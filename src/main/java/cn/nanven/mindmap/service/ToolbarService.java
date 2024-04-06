package cn.nanven.mindmap.service;

import cn.nanven.mindmap.dao.NodeDao;
import cn.nanven.mindmap.modal.NodeEntity;
import cn.nanven.mindmap.store.StoreManager;
import cn.nanven.mindmap.util.StyleUtil;
import cn.nanven.mindmap.view.NodeView;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class ToolbarService {
    private static ToolbarService instance;
    private NodeEntity node;
    private Button undoBtn;
    private Button redoBtn;
    private ToggleButton boldBtn;
    private ToggleButton italicBtn;
    private ToggleButton underlineBtn;
    private Button decFontSizeBtn;
    private Label fontSizeLabel;
    private Button incFontSizeBtn;
    private MenuButton alignMenuBtn;
    private Region alignMenuIcon;
    private Region fontColorIcon;
    private ColorPicker fontColorPicker;
    private ColorPicker backgroundColorPicker;
    private Button deleteBtn;
    private MenuButton addMenuBtn;

    private ToolbarService() {
    }

    private ToolbarService(BorderPane toolbar) {
        this.undoBtn = (Button) toolbar.lookup("#undo-btn");
        this.redoBtn = (Button) toolbar.lookup("#redo-btn");
        this.boldBtn = (ToggleButton) toolbar.lookup("#bold-btn");
        this.italicBtn = (ToggleButton) toolbar.lookup("#italic-btn");
        this.underlineBtn = (ToggleButton) toolbar.lookup("#underline-btn");
        this.decFontSizeBtn = (Button) toolbar.lookup("#dec-font-size-btn");
        this.fontSizeLabel = (Label) toolbar.lookup("#font-size-label");
        this.incFontSizeBtn = (Button) toolbar.lookup("#inc-font-size-btn");
        this.alignMenuBtn = (MenuButton) toolbar.lookup("#align-menu-btn");
        this.alignMenuIcon = (Region) toolbar.lookup("#align-menu-icon");
        this.fontColorPicker = (ColorPicker) toolbar.lookup("#font-color-picker");
        this.fontColorIcon = (Region) toolbar.lookup("#font-color-icon");
        this.backgroundColorPicker = (ColorPicker) toolbar.lookup("#background-color-picker");
        this.deleteBtn = (Button) toolbar.lookup("#delete-btn");
        this.addMenuBtn = (MenuButton) toolbar.lookup("#add-menu-btn");

        setDisable(true);
        addListener();
    }

    public static ToolbarService getInstance() {
        return instance;
    }

    public static void init(BorderPane toolbar) {
        if (null == instance) {
            instance = new ToolbarService(toolbar);
        }
    }

    private void addListener() {
        this.addMenuBtn.getItems().forEach(item -> {
            if (item.getId().equals("add-sub-menu-item")) {
                item.setOnAction(e -> {
                    NodeService.getInstance().addSubNode();
                });
            } else if (item.getId().equals("add-bro-menu-item")) {
                item.setOnAction(e -> {
                    NodeService.getInstance().addBroNode();
                });
            }
        });
        this.alignMenuBtn.getItems().forEach(item -> {
            if (item.getId().equals("align-left-menu-item")) {
                item.setOnAction(e -> {
                    node.setAlignment(Pos.CENTER_LEFT);
                    syncState();
                });
            } else if (item.getId().equals("align-center-menu-item")) {
                item.setOnAction(e -> {
                    node.setAlignment(Pos.CENTER);
                    syncState();
                });
            } else if (item.getId().equals("align-right-menu-item")) {
                item.setOnAction(e -> {
                    node.setAlignment(Pos.CENTER_RIGHT);
                    syncState();
                });
            }
        });
        this.boldBtn.setOnAction(e -> {
            if (this.boldBtn.isSelected()) {
                node.setFont(StyleUtil.mergeFont(node.getFont(), FontWeight.BOLD));
            } else node.setFont(StyleUtil.mergeFont(node.getFont(), FontWeight.NORMAL));
        });
        this.italicBtn.setOnAction(e -> {
            if (this.italicBtn.isSelected()) {
                node.setFont(StyleUtil.mergeFont(node.getFont(), FontPosture.ITALIC));
            } else node.setFont(StyleUtil.mergeFont(node.getFont(), FontPosture.REGULAR));
        });
        this.underlineBtn.setOnAction(e -> {
            node.setFontUnderline(this.underlineBtn.isSelected());
        });
        this.incFontSizeBtn.setOnAction(e -> {
            double size = Double.parseDouble(this.fontSizeLabel.getText());
            if (size < 56) size++;
            node.setFont(StyleUtil.mergeFont(node.getFont(), size));
            syncState();
        });
        this.decFontSizeBtn.setOnAction(e -> {
            double size = Double.parseDouble(this.fontSizeLabel.getText());
            if (size > 8) size--;
            node.setFont(StyleUtil.mergeFont(node.getFont(), size));
            syncState();
        });
        this.fontColorPicker.valueProperty().addListener((observableValue, color, t1) -> {
            node.setColor(t1);
            syncState();
        });
        this.backgroundColorPicker.valueProperty().addListener((observableValue, color, t1) -> {
            node.setBackground(StyleUtil.newBackground(t1.toString().substring(2)));
            syncState();
        });
        this.deleteBtn.setOnAction(e->{
            NodeEntity node=StoreManager.getSelectedNode().getNodeEntity();
            NodeDao.deleteNode(node);
        });
        this.undoBtn.setOnAction(e -> {


        });
        this.redoBtn.setOnAction(e -> {


        });
    }

    private void setDisable(Boolean state) {
        if (state) {
            this.boldBtn.setDisable(true);
            this.italicBtn.setDisable(true);
            this.underlineBtn.setDisable(true);
            this.decFontSizeBtn.setDisable(true);
            this.incFontSizeBtn.setDisable(true);
            this.alignMenuBtn.setDisable(true);
            this.fontColorPicker.setDisable(true);
            this.backgroundColorPicker.setDisable(true);

            this.fontSizeLabel.setDisable(true);
            this.deleteBtn.setDisable(true);
            this.alignMenuIcon.getStyleClass().add("disabled");
            this.fontColorIcon.getStyleClass().add("disabled");
        } else {
            this.boldBtn.setDisable(false);
            this.italicBtn.setDisable(false);
            this.underlineBtn.setDisable(false);
            this.decFontSizeBtn.setDisable(false);
            this.incFontSizeBtn.setDisable(false);
            this.alignMenuBtn.setDisable(false);
            this.fontColorPicker.setDisable(false);
            this.backgroundColorPicker.setDisable(false);

            this.fontSizeLabel.setDisable(false);
            this.deleteBtn.setDisable(false);

            this.alignMenuIcon.getStyleClass().remove("disabled");
            this.fontColorIcon.getStyleClass().remove("disabled");
        }

    }

    public void syncState() {
        NodeView selectedNode = StoreManager.getSelectedNode();

        if (selectedNode == null) {
            setDisable(true);
            return;
        }

        node = selectedNode.getNodeEntity();

        boolean underline = node.isFontUnderline();
        boolean bold = node.getFont().getStyle().contains("Bold");
        boolean italic = node.getFont().getStyle().contains("Italic");
        int fontSize = (int) node.getFont().getSize();
        String backgroundColor = StyleUtil.getBackgroundColor(node.getBackground());
        String fontColor = node.getColor().toString();
        Pos alignment = node.getAlignment();

        this.boldBtn.setSelected(bold);
        this.underlineBtn.setSelected(underline);
        this.italicBtn.setSelected(italic);

        this.fontSizeLabel.setText(Integer.toString(fontSize));
        this.fontColorIcon.setBackground(new Background(new BackgroundFill(Paint.valueOf(fontColor), null, null)));
        this.fontColorPicker.setValue(Color.valueOf(fontColor));
        this.backgroundColorPicker.setValue(Color.valueOf(backgroundColor));

        switch (alignment) {
            case CENTER -> {
                this.alignMenuIcon.getStyleClass().removeAll("align-center-icon", "align-left-icon", "align-right-icon");
                this.alignMenuIcon.getStyleClass().addAll("align-center-icon");
            }
            case CENTER_LEFT -> {
                this.alignMenuIcon.getStyleClass().removeAll("align-center-icon", "align-left-icon", "align-right-icon");
                this.alignMenuIcon.getStyleClass().addAll("align-left-icon");
            }
            case CENTER_RIGHT -> {
                this.alignMenuIcon.getStyleClass().removeAll("align-center-icon", "align-left-icon", "align-right-icon");
                this.alignMenuIcon.getStyleClass().addAll("align-right-icon");
            }
        }

        setDisable(false);
    }

}
