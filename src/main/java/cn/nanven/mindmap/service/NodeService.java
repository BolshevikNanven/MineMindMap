package cn.nanven.mindmap.service;

import cn.nanven.mindmap.dao.NodeDao;
import cn.nanven.mindmap.modal.NodeEntity;
import cn.nanven.mindmap.service.layout.LayoutFactory;
import cn.nanven.mindmap.service.sidebar.OutlineService;
import cn.nanven.mindmap.store.StoreManager;
import cn.nanven.mindmap.view.NodeView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class NodeService {
    private static NodeService instance;
    private Pane canvas;
    private ScrollPane container;
    private LayoutService layoutService;
    private OutlineService outline;

    private NodeService() {

    }

    private NodeService(ScrollPane container, Pane canvas,TreeView<String> outlineTreeView) {
        this.canvas = canvas;
        this.container = container;
        this.outline = new OutlineService(outlineTreeView);
        canvas.setFocusTraversable(true);
        canvas.setOnMouseClicked(e -> {
            canvas.requestFocus();
            if (e.getButton() == MouseButton.PRIMARY) {
                selectNode(null);
            }
        });
        layoutService = LayoutFactory.getInstance().getService("mindMap");
    }

    public static NodeService getInstance() {
        return instance;
    }

    public static void init(ScrollPane container, Pane canvas, TreeView<String> outlineTreeView) {
        if (null == instance) {
            instance = new NodeService(container, canvas,outlineTreeView);
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
                layoutService.layout();
                selectNode(nodeView);
                nodeView.focusText();
                newNode.actualHeightProperty().removeListener(this);
                outline.buildOutlineFromNode(newNode);

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
            outline.buildOutlineFromNode(newNode.getNodeEntity());
        }
        StoreManager.setSelectedNode(newNode);
        ToolbarService.getInstance().syncState();
    }

    public void dragNode(NodeView node, double prevX, double prevY, MouseEvent e) {
        if (node.getCursor() == Cursor.SE_RESIZE) {
            node.getNodeEntity().widthProperty().set(e.getSceneX() - node.getLayoutX());
            node.getNodeEntity().heightProperty().set(e.getSceneY() - node.getLayoutY() - 100);
        } else if (node.getCursor() == Cursor.E_RESIZE) {
            node.getNodeEntity().widthProperty().set(e.getSceneX() - node.getLayoutX());
        } else if (node.getCursor() == Cursor.S_RESIZE) {
            node.getNodeEntity().heightProperty().set(e.getSceneY() - node.getLayoutY() - 100);
        } else {
            Bounds viewportBounds = container.getViewportBounds();
            double offsetY, offsetX;
            offsetX = e.getSceneX() - prevX;
            offsetY = e.getSceneY() - prevY;

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

            node.getNodeEntity().xProperty().set(offsetX);
            node.getNodeEntity().yProperty().set(offsetY);
        }
    }
    public void dragDoneNode(NodeView nodeView, MouseEvent e){
        layoutService.layout();
    }

    public void removeNode(NodeView node) {
        canvas.getChildren().remove(node);
        outline.buildOutlineFromNode(node.getNodeEntity());
        layoutService.layout();
    }

}
