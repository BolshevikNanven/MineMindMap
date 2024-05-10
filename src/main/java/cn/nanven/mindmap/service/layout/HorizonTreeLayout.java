package cn.nanven.mindmap.service.layout;

import cn.nanven.mindmap.common.enums.Direction;
import cn.nanven.mindmap.dao.NodeDao;
import cn.nanven.mindmap.entity.NodeEntity;
import cn.nanven.mindmap.service.LayoutService;
import cn.nanven.mindmap.service.LineService;
import cn.nanven.mindmap.store.SystemStore;
import cn.nanven.mindmap.util.AlgorithmUtil;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;

import java.util.List;

public class HorizonTreeLayout implements LayoutService {
    private final Double MARGIN_V = 16.0;
    private final Double MARGIN_H = 56.0;
    private final Pane canvas;
    private final Pane indicator;
    private NodeEntity parent;
    private NodeEntity brother;
    private Direction nodeDirection;
    private Direction layoutDirection;

    public HorizonTreeLayout(Pane canvas, Direction direction) {
        this.canvas = canvas;
        this.indicator = new Pane();
        this.indicator.setPrefHeight(4);
        this.indicator.setVisible(false);
        this.layoutDirection = direction;

        canvas.getChildren().add(indicator);
    }

    //TODO:BUG: node children可能为null,原因不明
    private void doBounds(NodeEntity node) {
        double bounds = 0.0;
        if (node.getChildren() == null || node.getChildren().isEmpty()) {
            bounds = node.getActualHeight();
        } else {
            for (int i = 0; i < node.getChildren().size(); i++) {
                NodeEntity child = node.getChildren().get(i);
                doBounds(child);
                if (bounds == 0.0) {
                    bounds += child.getBounds();
                } else bounds += child.getBounds() + MARGIN_V;


                //最后一个子节点特殊处理，不计下边空白高度
                //if (i == node.getChildren().size() - 1 && i != 0) {
                //    bounds -= child.getBounds() / 2 - child.getActualHeight() / 2;
                //}

            }
        }

        //子节点高度和小于此节点高度
        if (bounds < node.getActualHeight()) {
            bounds = node.getActualHeight();
        }
        node.setBounds(bounds);
    }

    private void doLayout(NodeEntity node) {
        if (node.getParent() != null) {
            List<NodeEntity> broNodeList = node.getParent().getChildren();
            NodeEntity parent = node.getParent();
            int index = broNodeList.indexOf(node);
            double top = parent.getY() + parent.getActualHeight() / 2 - parent.getBounds() / 2;

            for (int i = 0; i < index; i++) {
                top += broNodeList.get(i).getBounds() + MARGIN_V;
            }


            top += node.getBounds() / 2 - node.getActualHeight() / 2;

            node.setY(top);
            if (layoutDirection == Direction.RIGHT) {
                node.setX(parent.getX() + parent.getActualWidth() + MARGIN_H);
            } else {
                node.setX(parent.getX() - node.getActualWidth() - MARGIN_H);
            }
        }
        if (node.getChildren() != null) {
            for (NodeEntity child : node.getChildren()) {
                doLayout(child);
            }
        }

    }

    @Override
    public void layout() {
        for (NodeEntity root : SystemStore.getRootNodeList()) {
            doBounds(root);
            doLayout(root);
        }
    }

    @Override
    public void indicate(NodeEntity node, double x, double y) {
        double bottom = y - node.getY();
        double right = x - node.getX();


        if (Math.abs(bottom) > node.getActualHeight() + 64 || Math.abs(right) > node.getActualWidth() + 72) {
            parent = null;
            brother = null;
            indicator.setVisible(false);
            return;
        }

        indicator.setBackground(node.getBackground());
        indicator.setPrefWidth(node.getActualWidth());

        if (right > node.getActualWidth() / 2 && layoutDirection == Direction.RIGHT) {
            //右吸附
            indicator.setLayoutX(node.getX() + node.getActualWidth());
            indicator.setLayoutY(node.getY() + node.getActualHeight() / 2 - 2);

            parent = node;
            brother = null;
        } else if (right < 0 && layoutDirection == Direction.LEFT) {
            //左吸附
            indicator.setLayoutX(node.getX() - indicator.getWidth());
            indicator.setLayoutY(node.getY() + node.getActualHeight() / 2 - 2);

            parent = node;
            brother = null;
        } else if (bottom > 0) {
            //下吸附
            indicator.setLayoutX(node.getX());
            indicator.setLayoutY(node.getY() + node.getActualHeight() + (MARGIN_V - indicator.getHeight()) / 2);

            parent = node.getParent();
            brother = node;
            nodeDirection = Direction.BOTTOM;
        } else if (bottom < 0) {
            //上吸附
            indicator.setLayoutX(node.getX());
            indicator.setLayoutY(node.getY() - (MARGIN_V + indicator.getHeight()) / 2);

            parent = node.getParent();
            brother = node;
            nodeDirection = Direction.TOP;
        }
        indicator.setVisible(true);

    }

    //TODO:优化逻辑
    @Override
    public void snap(NodeEntity node, double x, double y, double prevX, double prevY) {
        //节点不吸附直接移动
        if (parent == null || parent == node || brother == node || node.getParent() == null) {
            node.setX(x - prevX + node.getX());
            node.setY(y - prevY + node.getY());
        }

        ///节点吸附自身，结束
        if (parent == node || brother == node) {
            indicator.setVisible(false);
            return;
        }

        if (parent == null && node.getParent() != null) {   //节点取消吸附
            node.getParent().getChildren().remove(node);
            node.setParent(null);
            LineService.getInstance().deleteLine(node.getLine());
            node.setLine(null);

            SystemStore.getRootNodeList().add(node);
        } else if (parent != null && brother == null) {  //节点左右吸附
            if (AlgorithmUtil.checkExistParent(parent, node)) {
                indicator.setVisible(false);
                return;
            }
            NodeDao.moveNode(node, parent, 0);

            SystemStore.getRootNodeList().remove(node);
        } else if (parent != null && brother != null) {   //节点上下吸附
            int broIndex = brother.getParent().getChildren().indexOf(brother);
            NodeDao.moveNode(node, parent, nodeDirection == Direction.TOP ? broIndex : broIndex + 1);

            SystemStore.getRootNodeList().remove(node);
        }
        indicator.setVisible(false);

    }

    @Override
    public SimpleDoubleProperty[] getLineHead(NodeEntity node) {
        if (node == null) return null;

        SimpleDoubleProperty[] res = new SimpleDoubleProperty[]{new SimpleDoubleProperty(), new SimpleDoubleProperty()};
        if (layoutDirection == Direction.RIGHT) {
            res[0].set(node.getX() + node.getActualWidth());
            node.xProperty().addListener((observableValue, number, t1) -> {
                res[0].set(node.getX() + node.getActualWidth());
            });
        } else {
            res[0].set(node.getX());
            node.xProperty().addListener((observableValue, number, t1) -> {
                res[0].set(node.getX());
            });
        }
        res[1].set(node.getY() + node.getActualHeight() / 2);
        node.yProperty().addListener((observableValue, number, t1) -> {
            res[1].set(node.getY() + node.getActualHeight() / 2);
        });
        return res;
    }

    @Override
    public SimpleDoubleProperty[] getLineTail(NodeEntity node) {
        SimpleDoubleProperty[] res = new SimpleDoubleProperty[]{new SimpleDoubleProperty(), new SimpleDoubleProperty()};
        if (layoutDirection == Direction.RIGHT) {
            res[0].set(node.getX());
            node.xProperty().addListener((observableValue, number, t1) -> {
                res[0].set(node.getX());
            });
        } else {
            res[0].set(node.getX() + node.getActualWidth());
            node.xProperty().addListener((observableValue, number, t1) -> {
                res[0].set(node.getX() + node.getActualWidth());
            });
        }
        res[1].set(node.getY() + node.getActualHeight() / 2);
        node.yProperty().addListener((observableValue, number, t1) -> {
            res[1].set(node.getY() + node.getActualHeight() / 2);
        });
        return res;
    }
}
