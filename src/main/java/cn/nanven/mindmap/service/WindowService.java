package cn.nanven.mindmap.service;

import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class WindowService {
    private static WindowService instance;
    private Parent root;
    private Stage stage;
    private BorderPane base;
    private Button miniBtn;
    private Button scaleBtn;
    private Button closeBtn;
    private double windowX;
    private double windowY;
    private double windowWidth;
    private double windowHeight;
    private boolean isMax;

    private WindowService() {
    }

    private WindowService(Parent root, Stage stage) {
        this.root = root;
        this.stage = stage;
        this.base= (BorderPane) root.lookup("#base");
        this.miniBtn = (Button) root.lookup("#window-mini-btn");
        this.scaleBtn = (Button) root.lookup("#window-scale-btn");
        this.closeBtn = (Button) root.lookup("#window-close-btn");

        this.windowX = stage.getX();
        this.windowY = stage.getY();
        this.windowWidth = stage.getWidth();
        this.windowHeight = stage.getHeight();

        addListener();
    }

    public static void init(Parent root, Stage stage) {
        if (null == instance) {
            instance = new WindowService(root, stage);
        }
    }

    public WindowService getInstance() {
        return instance;
    }

    private void addListener() {
        this.miniBtn.setOnAction(e -> {
            stage.setIconified(true);
        });

        this.scaleBtn.setOnAction(e -> {
            if (isMax) {
                stage.setX(windowX);
                stage.setY(windowY);
                stage.setWidth(windowWidth);
                stage.setHeight(windowHeight);
                base.getStyleClass().remove("max");
                isMax = false;
            } else {
                Rectangle2D rectangle2d = Screen.getPrimary().getVisualBounds();
                stage.setX(rectangle2d.getMinX());
                stage.setY(rectangle2d.getMinY());
                stage.setWidth(rectangle2d.getWidth());
                stage.setHeight(rectangle2d.getHeight());
                base.getStyleClass().add("max");
                isMax = true;
            }
        });
        this.closeBtn.setOnAction(e -> {
            stage.close();
        });
    }

}
