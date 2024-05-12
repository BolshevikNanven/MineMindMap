package cn.nanven.mindmap.view;

import cn.nanven.mindmap.service.NodeService;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class NodeContext extends ContextMenu {
    private NodeView node;
    private MenuItem deleteMenu;
    private MenuItem addSubNodeMenu;
    private MenuItem addBroNodeMenu;
    private MenuItem applyStyleMenu;

    public NodeContext() {
        deleteMenu = new MenuItem("", generateMenu("", "删除", "Delete"));
        addSubNodeMenu = new MenuItem("", generateMenu("", "添加子节点", "Tab"));
        addBroNodeMenu = new MenuItem("", generateMenu("", "添加兄弟节点", "Enter"));
        applyStyleMenu = new MenuItem("", generateMenu("", "应用样式", ""));

        this.getItems().addAll(deleteMenu, addSubNodeMenu, addBroNodeMenu, applyStyleMenu);

        addListener();
    }

    public NodeContext render(NodeView node, double x, double y) {
        this.node = node;
        this.show(node, x, y);
        return this;
    }

    private void addListener() {
        deleteMenu.setOnAction(actionEvent -> {
            NodeService.getInstance().deleteNode(node.getNodeEntity());
        });
        addBroNodeMenu.setOnAction(actionEvent -> {
            NodeService.getInstance().addBroNode();
        });
        addSubNodeMenu.setOnAction(actionEvent -> {
            NodeService.getInstance().addSubNode();
        });
        applyStyleMenu.setOnAction(actionEvent -> {
            NodeService.getInstance().applyStyle(node.getNodeEntity());
        });
    }

    private HBox generateMenu(String icon, String title, String key) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("title");
        titleLabel.setPrefWidth(86);

        Label keyLabel = new Label(key);
        keyLabel.getStyleClass().add("description");

        return new HBox(titleLabel, keyLabel);

    }
}
