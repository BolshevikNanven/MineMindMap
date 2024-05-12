package cn.nanven.mindmap.dao;

import cn.nanven.mindmap.common.handler.MapEventHandler;
import cn.nanven.mindmap.entity.NodeEntity;
import cn.nanven.mindmap.service.LineService;
import cn.nanven.mindmap.service.ToolbarService;
import cn.nanven.mindmap.store.SystemStore;
import cn.nanven.mindmap.util.AlgorithmUtil;
import cn.nanven.mindmap.util.StyleUtil;
import javafx.geometry.Pos;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

public class NodeDao {
    private static final List<NodeEntity> rootNodeList = SystemStore.getRootNodeList();

    private static void doDeleteNode(NodeEntity node) {
        if (!node.getChildren().isEmpty()) {
            for (NodeEntity child : node.getChildren()) {
                doDeleteNode(child);
            }
        }
        node.setParent(null);
        node.setChildren(null);
        node.setDeleteSymbol(true);
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
            } else {
                SystemStore.getRootNodeList().remove(node);
            }
            parent.getChildren().add(index, node);
            node.setParent(parent);
            LineService.getInstance().addLine(parent, node);
        }

    }

    public static void treeApplyStyle(NodeEntity node) {
        AlgorithmUtil.headMapNode(node, ((parent, node1) -> {
            if (parent != null) {
                copyStyle(parent,node1);
            }
        }));

    }

    public static void copyStyle(NodeEntity source, NodeEntity target) {
        target.setAlignment(source.getAlignment());
        target.setBorder(source.getBorder());
        target.setBackground(source.getBackground());
        target.setColor(source.getColor());
        target.setFont(source.getFont());
        target.setFontUnderline(source.isFontUnderline());
        target.setHeight(source.getHeight());
        target.setWidth(source.getWidth());
    }

    public static void deleteNode(NodeEntity node) {
        if (node.getParent() == null) {
            SystemStore.getRootNodeList().remove(node);
        } else {
            node.getParent().getChildren().remove(node);
        }
        SystemStore.setSelectedNode(null);
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
            node.setBorder(StyleUtil.newBorder("transparent"));
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
            node.setBorder(template.getBorder());
            node.setColor(template.getColor());
            node.setFont(template.getFont());
            node.setFontUnderline(template.isFontUnderline());
        }

        return node;
    }
}
