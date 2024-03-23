package cn.nanven.mindmap.service.layout;

import cn.nanven.mindmap.modal.NodeEntity;
import cn.nanven.mindmap.service.LayoutService;
import cn.nanven.mindmap.store.StoreManager;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

import java.util.List;

public class MindMapLayout implements LayoutService {
    private final Pane canvas;
    private final Double MARGIN_V = 16.0;
    private final Double MARGIN_H = 64.0;

    public MindMapLayout(Pane canvas) {
        this.canvas = canvas;
    }

    private void doBounds(NodeEntity node) {
        double bounds = 0.0;
        if (node.getChildren().isEmpty()) {
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
        for (NodeEntity child : node.getChildren()) {
            doLayout(child);
        }
    }

    @Override
    public void layout() {
        for (NodeEntity root : StoreManager.getRootNodeList()) {
            doBounds(root);
            doLayout(root);
        }
    }

}
