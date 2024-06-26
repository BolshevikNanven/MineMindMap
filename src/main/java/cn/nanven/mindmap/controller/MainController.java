package cn.nanven.mindmap.controller;

import cn.nanven.mindmap.service.*;
import cn.nanven.mindmap.service.layout.LayoutFactory;
import cn.nanven.mindmap.service.sidebar.SidebarFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private ScrollPane canvasContainer;
    @FXML
    private Pane canvas;
    @FXML
    private BorderPane toolbar;
    @FXML
    private HBox sideBarTab;
    @FXML
    private BorderPane sidebar;
    @FXML
    private AnchorPane sidebarContent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SidebarFactory.init(sidebarContent);
        LayoutFactory.init(canvas);

        CanvasService.init(canvasContainer, canvas);
        UndoAndRedoService.init();
        LineService.init(canvas);
        NodeService.init(canvasContainer, canvas);
        ToolbarService.init(toolbar);

        SidebarController.init(sideBarTab, sidebar, sidebarContent);
    }
}