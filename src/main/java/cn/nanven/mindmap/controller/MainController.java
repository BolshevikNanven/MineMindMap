package cn.nanven.mindmap.controller;

import cn.nanven.mindmap.service.LineService;
import cn.nanven.mindmap.service.NodeService;
import cn.nanven.mindmap.service.ToolbarService;
import cn.nanven.mindmap.service.layout.LayoutFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    protected ScrollPane canvasContainer;
    @FXML
    protected Pane canvas;
    @FXML
    protected BorderPane toolbar;
    @FXML
    protected HBox sideBarTab;
    @FXML
    protected BorderPane sidebar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new SidebarController(sideBarTab, sidebar);

        LayoutFactory.init(canvas);
        LineService.init(canvas);
        NodeService.init(canvasContainer, canvas);
        ToolbarService.init(toolbar);
    }
}