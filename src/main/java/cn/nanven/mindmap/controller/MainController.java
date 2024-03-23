package cn.nanven.mindmap.controller;

import cn.nanven.mindmap.service.LineService;
import cn.nanven.mindmap.service.NodeService;
import cn.nanven.mindmap.service.ToolbarService;
import cn.nanven.mindmap.service.layout.LayoutFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
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
    private Button closeBtn;
    @FXML
    private Button miniBtn;
    @FXML
    private Button wideBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LayoutFactory.init(canvas);
        LineService.init(canvas);
        NodeService.init(canvasContainer, canvas);
        ToolbarService.init(toolbar);
    }
}