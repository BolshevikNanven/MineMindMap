package cn.nanven.mindmap.service.sidebar;

import cn.nanven.mindmap.modal.NodeEntity;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.List;

public class OutlineService {
    private TreeView<String> outlineTreeView;


    public OutlineService()
    {

    }
    public OutlineService(TreeView<String> outlineTreeView)
    {
        this.outlineTreeView = outlineTreeView;
    }

    public TreeView<String> getOutlineTreeView() {
        return outlineTreeView;
    }

    public void setOutlineTreeView(TreeView<String> outlineTreeView) {
        this.outlineTreeView = outlineTreeView;
    }

    private NodeEntity findRootNode(NodeEntity node) {
        NodeEntity current = node;
        while (current != null && current.getParent() != null) {
            current = current.getParent();
        }
        return current;
    }

    // 构建大纲
    public  void buildOutlineFromNode(NodeEntity node) {
        NodeEntity rootNode = findRootNode(node);
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


    public void updateOutline(NodeEntity node) {
        NodeEntity rootNode = findRootNode(node); // 寻找根节点
        outlineTreeView.getRoot().getChildren().clear();
        if (rootNode != null) {
            addTreeItems(outlineTreeView.getRoot(), rootNode.getChildren()); // 根据新的根节点重建大纲
        }
    }
}
