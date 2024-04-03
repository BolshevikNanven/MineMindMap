package cn.nanven.mindmap.service.sidebar;

import cn.nanven.mindmap.modal.NodeEntity;
import cn.nanven.mindmap.service.SidebarService;
import cn.nanven.mindmap.store.StoreManager;
import cn.nanven.mindmap.util.AlgorithmUtil;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.List;

public class OutlineService implements SidebarService {
    private final TreeView<String> outlineTreeView;

    public OutlineService() {
        outlineTreeView = new TreeView<>();
    }

    // 构建大纲
    private void buildOutlineFromNode(NodeEntity node) {
        NodeEntity rootNode = AlgorithmUtil.findRoot(node);
        if (rootNode != null) {
            TreeItem<String> rootItem = new TreeItem<>(rootNode.getContent());
            outlineTreeView.setRoot(rootItem);
            addTreeItems(rootItem, rootNode.getChildren());
            outlineTreeView.setShowRoot(true);
        }
    }

    // 递归地添加子节点到TreeView
    private void addTreeItems(TreeItem<String> parentItem, List<NodeEntity> children) {
        if (children == null) return;

        for (NodeEntity child : children) {
            TreeItem<String> item = new TreeItem<>(child.getContent());
            parentItem.getChildren().add(item);
            if (child.getChildren() != null && !child.getChildren().isEmpty()) {
                addTreeItems(item, child.getChildren());
            }
        }
    }


    private void updateOutline() {
        for (NodeEntity node : StoreManager.getRootNodeList()) {
            outlineTreeView.setRoot(new TreeItem<>(node.getContent()));
            addTreeItems(outlineTreeView.getRoot(), node.getChildren()); // 根据新的根节点重建大纲
        }
    }

    @Override
    public Node render() {
        updateOutline();
        return outlineTreeView;
    }

    @Override
    public void sync() {
        updateOutline();
    }
}
