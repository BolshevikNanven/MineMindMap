package cn.nanven.mindmap.service;

import cn.nanven.mindmap.controller.SidebarController;
import cn.nanven.mindmap.dao.NodeDao;
import cn.nanven.mindmap.entity.NodeEntity;
import cn.nanven.mindmap.service.layout.LayoutFactory;
import cn.nanven.mindmap.store.StoreManager;
import cn.nanven.mindmap.util.AlgorithmUtil;
import cn.nanven.mindmap.view.AuxiliaryNodeView;
import cn.nanven.mindmap.view.NodeView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class NodeService {
    private static NodeService instance;
    private Pane canvas;
    private ScrollPane container;
    private LayoutService layoutService;

    private NodeService() {

    }

    private NodeService(ScrollPane container, Pane canvas) {
        this.canvas = canvas;
        this.container = container;
        canvas.setFocusTraversable(true);
        canvas.setOnMouseClicked(e -> {
            canvas.requestFocus();
            if (e.getButton() == MouseButton.PRIMARY) {
                selectNode(null);
            }
        });
        layoutService = LayoutFactory.getInstance().getService("mindMap");
        StoreManager.setAuxiliaryNode(new AuxiliaryNodeView(canvas));
    }

    public static NodeService getInstance() {
        return instance;
    }

    public static void init(ScrollPane container, Pane canvas) {
        if (null == instance) {
            instance = new NodeService(container, canvas);
        }
    }


    private void addNode(NodeEntity parent, NodeEntity brother) {
        NodeEntity newNode;
        if (parent == null) {
            newNode = NodeDao.newNode();
        } else if (brother == null) {
            newNode = NodeDao.newNode(parent);
        } else {
            newNode = NodeDao.newNode(parent, brother);
        }

        NodeView nodeView = new NodeView(newNode);
        nodeView.setFocusTraversable(true);

        this.canvas.getChildren().add(nodeView);

        //等待节点渲染完成再进行布局
        newNode.actualHeightProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                LineService.getInstance().addLine(newNode.getParent(), newNode);
                SidebarController.getInstance().sync();
                layoutService.layout();
                selectNode(nodeView);
                nodeView.focusText();

                newNode.actualHeightProperty().removeListener(this);
            }
        });

    }

    public void addSubNode() {
        NodeView selectedNode = StoreManager.getSelectedNode();
        if (selectedNode == null) {
            addNode(null, null);
        } else {
            addNode(selectedNode.getNodeEntity(), null);
        }
    }

    public void addBroNode() {
        NodeView selectedNode = StoreManager.getSelectedNode();
        if (selectedNode == null) {
            addNode(null, null);
        } else {
            addNode(selectedNode.getNodeEntity().getParent(), selectedNode.getNodeEntity());
        }
    }

    public void selectNode(NodeView newNode) {
        NodeView prevNode = StoreManager.getSelectedNode();
        if (newNode == prevNode) {
            return;
        }
        if (prevNode != null) {
            prevNode.getStyleClass().removeAll("node-selected");
            prevNode.getNodeEntity().setDisabled(true);
        }
        if (newNode != null) {
            newNode.getStyleClass().addAll("node-selected");
        }
        StoreManager.setSelectedNode(newNode);
        ToolbarService.getInstance().syncState();
        SidebarController.getInstance().sync();
    }

    public void dragNode(NodeView node, double prevX, double prevY, MouseEvent e) {
        Point2D prevLocalCoords = canvas.sceneToLocal(prevX, prevY);
        Point2D localCoords = canvas.sceneToLocal(e.getSceneX(), e.getSceneY());

        if (node.getCursor() == Cursor.SE_RESIZE) {
            //右下角拉伸
            node.getNodeEntity().widthProperty().set(localCoords.getX() - node.getLayoutX());
            node.getNodeEntity().heightProperty().set(localCoords.getY() - node.getLayoutY());
        } else if (node.getCursor() == Cursor.E_RESIZE) {
            //右边拉伸
            node.getNodeEntity().widthProperty().set(localCoords.getX() - node.getLayoutX());
        } else if (node.getCursor() == Cursor.S_RESIZE) {
            //下边拉伸
            node.getNodeEntity().heightProperty().set(localCoords.getY() - node.getLayoutY());
        } else {
            //拖拽节点
            Bounds viewportBounds = container.getViewportBounds();
            double offsetX = localCoords.getX() - prevLocalCoords.getX() + node.getLayoutX();
            double offsetY = localCoords.getY() - prevLocalCoords.getY() + node.getLayoutY();

            //lambda变量
            final NodeEntity[] nearby = {null};
            final double[] distance = {0.0};

            //container跟随滚动
            if (e.getSceneX() > container.getLayoutX() + viewportBounds.getWidth()) {
                container.setHvalue(container.getHvalue() + (e.getSceneX() - container.getLayoutX() - viewportBounds.getWidth()) / viewportBounds.getWidth());
            } else if (e.getSceneX() < container.getLayoutX()) {
                container.setHvalue(container.getHvalue() - (container.getLayoutX() - e.getSceneX()) / viewportBounds.getWidth());
            }

            if (e.getSceneY() - 100 > container.getLayoutY() + viewportBounds.getHeight()) {
                container.setVvalue(container.getVvalue() + (e.getSceneY() - 100 - container.getLayoutY() - viewportBounds.getHeight()) / viewportBounds.getHeight());
            } else if (e.getSceneY() - 100 < container.getLayoutY()) {
                container.setVvalue(container.getVvalue() + (container.getLayoutY() + e.getSceneY() - 100) / viewportBounds.getHeight());
            }

            //拖拽辅助节点
            StoreManager.getAuxiliaryNode().move(offsetX, offsetY);
            node.setDisable(true);

            //遍历寻找吸附节点
            for (NodeEntity root : StoreManager.getRootNodeList()) {
                AlgorithmUtil.headMapNode(root, n -> {
                    double centerX = n.getX() + n.getActualWidth() / 2;
                    double centerY = n.getY() + n.getActualHeight() / 2;

                    double currentDistance = Math.hypot(e.getSceneX() - centerX, e.getSceneY() - 100 - centerY);
                    if (currentDistance <= distance[0] || distance[0] == 0.0) {
                        distance[0] = currentDistance;
                        nearby[0] = n;
                    }

                });
            }
            layoutService.indicate(nearby[0], localCoords.getX(), localCoords.getY());

        }
    }

    public void dragDoneNode(NodeView node, double prevX, double prevY, MouseEvent e) {
        //如果为拖拽节点
        if (node.getCursor() == Cursor.DEFAULT) {
            Point2D orgLocalCoords = canvas.sceneToLocal(prevX, prevY);
            Point2D localCoords = canvas.sceneToLocal(e.getSceneX(), e.getSceneY());

            StoreManager.getAuxiliaryNode().hide();
            layoutService.snap(node.getNodeEntity(), localCoords.getX(), localCoords.getY(), orgLocalCoords.getX(), orgLocalCoords.getY());
        }

        layoutService.layout();
        SidebarController.getInstance().sync();

        node.setDisable(false);
    }

    public void removeNode(NodeView node) {
        //移除node视图
        canvas.getChildren().remove(node);
        layoutService.layout();
        SidebarController.getInstance().sync();
    }

}
