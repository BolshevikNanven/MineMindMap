package cn.nanven.mindmap.dao;

import cn.nanven.mindmap.modal.NodeEntity;
import cn.nanven.mindmap.service.ToolbarService;
import cn.nanven.mindmap.store.StoreManager;
import cn.nanven.mindmap.util.StyleUtil;
import javafx.geometry.Pos;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

public class NodeDao {
    private static final List<NodeEntity> rootNodeList = StoreManager.getRootNodeList();

    private static void doDeleteNode(NodeEntity node) {
        if (!node.getChildren().isEmpty()) {
            for (NodeEntity child : node.getChildren()) {
                doDeleteNode(child);
            }
        }
        node.setParent(null);
        node.setChildren(null);
        node.delete();
    }

    public static void moveNode(NodeEntity node, NodeEntity parent, int index) {

        if (node.getParent() == parent) {
            int prevIndex = parent.getChildren().indexOf(node);
            parent.getChildren().remove(prevIndex);
            if (index > prevIndex) {
                parent.getChildren().add(index - 1, node);
            } else parent.getChildren().add(index, node);
        } else {
            if (node.getParent() != null) {
                node.getParent().getChildren().remove(node);
            }
            parent.getChildren().add(index, node);
            node.setParent(parent);
            node.getLine().setHead(parent);
        }

    }

    public static void deleteNode(NodeEntity node) {
        if (node.getParent() == null) {
            StoreManager.getRootNodeList().remove(node);
        } else {
            node.getParent().getChildren().remove(node);
        }
        StoreManager.setSelectedNode(null);
        ToolbarService.getInstance().syncState();
        doDeleteNode(node);
    }

    public static NodeEntity newNode(NodeEntity parent, NodeEntity prevNode) {
        NodeEntity node = newBean(prevNode);
        node.setParent(parent);
        node.setChildren(new ArrayList<>());

        parent.getChildren().add(node);
        return node;
    }

    public static NodeEntity newNode(NodeEntity parent) {
        NodeEntity node = newBean(parent);
        node.setParent(parent);
        node.setChildren(new ArrayList<>());

        parent.getChildren().add(node);
        return node;
    }

    public static NodeEntity newNode() {
        NodeEntity node = newBean(null);
        rootNodeList.add(node);
        node.setParent(null);
        node.setChildren(new ArrayList<>());

        return node;
    }

    private static NodeEntity newBean(NodeEntity template) {
        NodeEntity node = new NodeEntity();
        if (template == null) {
            node.setAlignment(Pos.CENTER_LEFT);
            node.setContent("新节点");
            node.setX(50);
            node.setY(50);
            node.setHeight(42);
            node.setWidth(86);
            node.setDisabled(false);
            node.setBackground(StyleUtil.newBackground("rgb(211,227,253)"));
            node.setColor(Paint.valueOf("#212121"));
            node.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 14));
            node.setFontUnderline(false);
        } else {
            node.setAlignment(template.getAlignment());
            node.setContent("新节点");
            node.setX(50);
            node.setY(50);
            node.setHeight(42);
            node.setDisabled(false);
            node.setWidth(template.getWidth());
            node.setBackground(template.getBackground());
            node.setColor(template.getColor());
            node.setFont(template.getFont());
            node.setFontUnderline(template.isFontUnderline());
        }

        return node;
    }
}
