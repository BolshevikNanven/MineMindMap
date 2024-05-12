package cn.nanven.mindmap.service.layout;

import cn.nanven.mindmap.common.enums.Direction;
import cn.nanven.mindmap.dao.NodeDao;
import cn.nanven.mindmap.entity.NodeEntity;
import cn.nanven.mindmap.service.LayoutService;
import cn.nanven.mindmap.service.LineService;
import cn.nanven.mindmap.store.SettingStore;
import cn.nanven.mindmap.store.SystemStore;
import cn.nanven.mindmap.util.AlgorithmUtil;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;

import java.util.List;

public class HorizonTreeLayout implements LayoutService {
    private final Double MARGIN_V = 16.0;
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

    private void doBounds(NodeEntity node) {
        // 直接检查子节点列表是否为空
        if (node.getChildren().isEmpty()) {
            node.setBounds(node.getActualHeight());
            return;
        }

        double totalHeight = 0.0;
        for (NodeEntity child : node.getChildren()) {
            doBounds(child);
            // 更新高度总和，考虑空白
            totalHeight += (totalHeight > 0 ? child.getBounds() + MARGIN_V : child.getBounds());
        }

        // 子节点高度和可能小于此节点高度
        node.setBounds(Math.max(totalHeight, node.getActualHeight()));
    }

    private void doLayout(NodeEntity node) {
        // 仅当节点有父级时，才处理布局
        if (node.getParent() != null) {
            NodeEntity parent = node.getParent();
            List<NodeEntity> siblings = parent.getChildren();

            // 提前计算父节点和所有子节点的相关位置，避免重复计算
            double parentCenterY = parent.getY() + parent.getActualHeight() / 2;
            double childrenTotalHeight = siblings.stream().mapToDouble(NodeEntity::getBounds).sum();
            double childrenSpacing = (siblings.size() - 1) * MARGIN_V;
            double top = parentCenterY - (childrenTotalHeight + childrenSpacing) / 2;

            for (NodeEntity child : siblings) {
                double childY = top + child.getBounds() / 2 - child.getActualHeight() / 2;
                double childX = layoutDirection == Direction.RIGHT ?
                        parent.getX() + parent.getActualWidth() + SettingStore.getMarginH() :
                        parent.getX() - child.getActualWidth() - SettingStore.getMarginH();

                // 直接设置计算得到的位置
                child.setY(childY);
                child.setX(childX);

                // 更新下一个子节点起始位置
                top += child.getBounds() + MARGIN_V;
            }
        }

        // 递归处理子节点
        node.getChildren().forEach(this::doLayout);
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
    public SimpleDoubleProperty[] getLineHead(NodeEntity head, NodeEntity tail) {
        if (head == null) return null;

        SimpleDoubleProperty[] res = new SimpleDoubleProperty[]{new SimpleDoubleProperty(), new SimpleDoubleProperty()};
        if (layoutDirection == Direction.RIGHT) {
            res[0].set(head.getX() + head.getActualWidth());
            head.xProperty().addListener((observableValue, number, t1) -> {
                res[0].set(head.getX() + head.getActualWidth());
            });
        } else {
            res[0].set(head.getX());
            head.xProperty().addListener((observableValue, number, t1) -> {
                res[0].set(head.getX());
            });
        }
        res[1].set(head.getY() + head.getActualHeight() / 2);
        head.yProperty().addListener((observableValue, number, t1) -> {
            res[1].set(head.getY() + head.getActualHeight() / 2);
        });
        return res;
    }

    @Override
    public SimpleDoubleProperty[] getLineTail(NodeEntity head, NodeEntity tail) {
        SimpleDoubleProperty[] res = new SimpleDoubleProperty[]{new SimpleDoubleProperty(), new SimpleDoubleProperty()};
        if (layoutDirection == Direction.RIGHT) {
            res[0].set(tail.getX());
            tail.xProperty().addListener((observableValue, number, t1) -> {
                res[0].set(tail.getX());
            });
        } else {
            res[0].set(tail.getX() + tail.getActualWidth());
            tail.xProperty().addListener((observableValue, number, t1) -> {
                res[0].set(tail.getX() + tail.getActualWidth());
            });
        }
        res[1].set(tail.getY() + tail.getActualHeight() / 2);
        tail.yProperty().addListener((observableValue, number, t1) -> {
            res[1].set(tail.getY() + tail.getActualHeight() / 2);
        });
        return res;
    }
}
