package cn.nanven.mindmap.service.layout;

import cn.nanven.mindmap.dao.NodeDao;
import cn.nanven.mindmap.modal.NodeEntity;
import cn.nanven.mindmap.service.LayoutService;
import cn.nanven.mindmap.store.StoreManager;
import cn.nanven.mindmap.util.AlgorithmUtil;
import javafx.scene.layout.Pane;

import java.util.List;

public class MindMapLayout implements LayoutService {
    private final Double MARGIN_V = 16.0;
    private final Double MARGIN_H = 64.0;
    private final Pane canvas;
    private final Pane indicator;
    private NodeEntity parent;
    private NodeEntity brother;
    private int direction;

    public MindMapLayout(Pane canvas) {
        this.canvas = canvas;
        this.indicator = new Pane();
        this.indicator.setPrefHeight(4);
        this.indicator.setVisible(false);

        canvas.getChildren().add(indicator);
    }

    //TODO:BUG: node children可能为null,原因不明
    private void doBounds(NodeEntity node) {
        double bounds = 0.0;
        if (node.getChildren() == null || node.getChildren().isEmpty()) {
            bounds = node.getActualHeight();
        } else {
            for (NodeEntity child : node.getChildren()) {
                doBounds(child);
                bounds += child.getBounds() + MARGIN_V;
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

            top += node.getBounds() / 2 - node.getActualHeight() / 2 + MARGIN_V / 2;
            node.setY(top);
            node.setX(parent.getX() + parent.getActualWidth() + MARGIN_H);

        }
        if(node.getChildren()!=null){
            for (NodeEntity child : node.getChildren()) {
                doLayout(child);
            }
        }

    }

    @Override
    public void layout() {
        for (NodeEntity root : StoreManager.getRootNodeList()) {
            doBounds(root);
            doLayout(root);
        }
    }

    @Override
    public void indicate(NodeEntity node, double x, double y) {
        double bottom = y - (node.getY() + node.getActualHeight() / 2);
        double right = x - (node.getX() + node.getActualWidth() / 2);

        if (Math.abs(bottom) > node.getActualHeight() + 64 || Math.abs(right) > node.getActualWidth() + 64) {
            parent = null;
            brother = null;
            indicator.setVisible(false);
            return;
        }

        indicator.setBackground(node.getBackground());
        indicator.setPrefWidth(node.getActualWidth());

        if (right > node.getActualWidth() / 2) {
            //右吸附
            indicator.setLayoutX(node.getX() + node.getActualWidth());
            indicator.setLayoutY(node.getY() + node.getActualHeight() / 2 - 2);

            parent = node;
            brother = null;
        } else if (bottom > 0) {
            //下吸附
            indicator.setLayoutX(node.getX());
            indicator.setLayoutY(node.getY() + node.getActualHeight() + (MARGIN_V - indicator.getHeight()) / 2);

            parent = node.getParent();
            brother = node;
            direction = 2;
        } else if (bottom < 0) {
            //上吸附
            indicator.setLayoutX(node.getX());
            indicator.setLayoutY(node.getY() - (MARGIN_V + indicator.getHeight()) / 2);

            parent = node.getParent();
            brother = node;
            direction = 0;
        }
        indicator.setVisible(true);

    }

    //TODO:优化逻辑
    @Override
    public void snap(NodeEntity node) {
        if (parent == node || brother == node) {
            indicator.setVisible(false);
            return;
        }

        if (parent == null && node.getParent() != null) {
            node.getParent().getChildren().remove(node);
            node.setParent(null);
            node.getLine().setHead(null);
            StoreManager.getRootNodeList().add(node);
        } else if (parent != null && brother == null) {
            if (AlgorithmUtil.checkExistParent(parent, node)) {
                indicator.setVisible(false);
                return;
            }
            NodeDao.moveNode(node, parent, 0);
        } else if (parent != null && brother != null) {
            int broIndex = brother.getParent().getChildren().indexOf(brother);
            NodeDao.moveNode(node, parent, direction == 0 ? broIndex : broIndex + 1);
        }
        indicator.setVisible(false);
    }

}
