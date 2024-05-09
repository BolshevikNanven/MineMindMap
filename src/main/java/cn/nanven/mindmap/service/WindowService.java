package cn.nanven.mindmap.service;

import cn.nanven.mindmap.store.StoreManager;
import cn.nanven.mindmap.store.ThreadsPool;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private ImageView loadingIndicator;
    private Label title;
    private double windowX;
    private double windowY;
    private double windowWidth;
    private double windowHeight;
    private boolean isMax;

    private double dragOffsetX;
    private double dragOffsetY;


    private static final double MIN_WIDTH = 900.0D;
    private static final double MIN_HEIGHT = 500.0D;
    private static final double BORDER = 12.0D;


    private WindowService() {
    }

    private WindowService(Parent root, Stage stage) {
        this.root = root;
        this.stage = stage;
        this.base = (BorderPane) root.lookup("#base");
        this.miniBtn = (Button) root.lookup("#window-mini-btn");
        this.scaleBtn = (Button) root.lookup("#window-scale-btn");
        this.closeBtn = (Button) root.lookup("#window-close-btn");
        this.loadingIndicator = (ImageView) root.lookup("#loading-indicator");
        this.title = (Label) root.lookup("#title");

        this.windowX = stage.getX();
        this.windowY = stage.getY();
        this.windowWidth = stage.getWidth();
        this.windowHeight = stage.getHeight();
        addListener();
        addResizeListeners();
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
        this.loadingIndicator.visibleProperty().bind(StoreManager.loadingStateProperty());
        //监听当前文件以更改标题
        StoreManager.file().addListener(observable -> {
            if (StoreManager.getFile() != null) {
                //回到ui线程进行渲染
                Platform.runLater(() -> {
                    String fullName = StoreManager.getFile().getName();
                    this.title.setText(fullName.substring(0, fullName.lastIndexOf('.')));
                });
            }
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
            ThreadsPool.close();
            stage.close();
        });
        stage.setOnCloseRequest(windowEvent -> {
            ThreadsPool.close();
        });
        root.setOnMousePressed(event -> {

            if (!isMax) {
                dragOffsetX = event.getSceneX();
                dragOffsetY = event.getSceneY();
            }
        });


        root.setOnMouseDragged(event -> {

            if (!isMax) {
                stage.setX(event.getScreenX() - dragOffsetX);
                stage.setY(event.getScreenY() - dragOffsetY);
            }
        });

    }

    private void addResizeListeners() {
        root.setOnMouseMoved(this::onMouseMoved);
        root.setOnMousePressed(this::onMousePressed);
        root.setOnMouseDragged(this::onMouseDragged);
    }

    private void onMouseMoved(MouseEvent event) {
        if (isMax) {
            return;
        }
        updateCursor(event);
    }

    private void onMousePressed(MouseEvent event) {
        if (isMax) {
            return;
        }
        if (root.getCursor() != Cursor.DEFAULT) {
            double xOffset = stage.getWidth() - event.getX();
            double yOffset = stage.getHeight() - event.getY();
        } else {
            dragOffsetX = event.getSceneX();
            dragOffsetY = event.getSceneY();
        }
    }

    private void onMouseDragged(MouseEvent event) {
        if (isMax) {
            return;
        }
        if (root.getCursor() != Cursor.DEFAULT) {
            double mouseX = event.getScreenX();
            double mouseY = event.getScreenY();
            double newWidth = stage.getWidth();
            double newHeight = stage.getHeight();


            switch (root.getCursor().toString()) {
                case "NW_RESIZE":
                    newWidth = stage.getX() + stage.getWidth() - mouseX;
                    newHeight = stage.getY() + stage.getHeight() - mouseY;
                    if (newWidth >= MIN_WIDTH) stage.setX(mouseX);
                    if (newHeight >= MIN_HEIGHT) stage.setY(mouseY);
                    break;
                case "NE_RESIZE":
                    newWidth = mouseX - stage.getX();
                    newHeight = stage.getY() + stage.getHeight() - mouseY;
                    if (newHeight >= MIN_HEIGHT) stage.setY(mouseY);
                    break;
                case "SW_RESIZE":
                    newWidth = stage.getX() + stage.getWidth() - mouseX;
                    newHeight = mouseY - stage.getY();
                    if (newWidth >= MIN_WIDTH) stage.setX(mouseX);
                    break;
                case "SE_RESIZE":
                    newWidth = mouseX - stage.getX();
                    newHeight = mouseY - stage.getY();
                    break;
                case "E_RESIZE":
                    newWidth = mouseX - stage.getX();
                    break;
                case "S_RESIZE":
                    newHeight = mouseY - stage.getY();
                    break;
                case "W_RESIZE":
                    newWidth = stage.getX() + stage.getWidth() - mouseX;
                    if (newWidth >= MIN_WIDTH) stage.setX(mouseX);
                    break;
                case "N_RESIZE":
                    newHeight = stage.getY() + stage.getHeight() - mouseY;
                    if (newHeight >= MIN_HEIGHT) stage.setY(mouseY);
                    break;
            }


            if (newWidth >= MIN_WIDTH) stage.setWidth(newWidth);
            if (newHeight >= MIN_HEIGHT) stage.setHeight(newHeight);
        } else {
            stage.setX(event.getScreenX() - dragOffsetX);
            stage.setY(event.getScreenY() - dragOffsetY);
        }
    }

    private void updateCursor(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        Cursor cursorType = Cursor.DEFAULT;
        if (x < BORDER && y < BORDER) {
            cursorType = Cursor.NW_RESIZE;
        } else if (x > root.getBoundsInLocal().getWidth() - BORDER && y < BORDER) {
            cursorType = Cursor.NE_RESIZE;
        } else if (x < BORDER && y > root.getBoundsInLocal().getHeight() - BORDER) {
            cursorType = Cursor.SW_RESIZE;
        } else if (x > root.getBoundsInLocal().getWidth() - BORDER && y > root.getBoundsInLocal().getHeight() - BORDER) {
            cursorType = Cursor.SE_RESIZE;
        } else if (x < BORDER) {
            cursorType = Cursor.W_RESIZE;
        } else if (x > root.getBoundsInLocal().getWidth() - BORDER) {
            cursorType = Cursor.E_RESIZE;
        } else if (y < BORDER) {
            cursorType = Cursor.N_RESIZE;
        } else if (y > root.getBoundsInLocal().getHeight() - BORDER) {
            cursorType = Cursor.S_RESIZE;
        }
        root.setCursor(cursorType);
    }


}
