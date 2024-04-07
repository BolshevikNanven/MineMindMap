package cn.nanven.mindmap.service;

import cn.nanven.mindmap.store.StoreManager;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

public class CanvasService {
    private static CanvasService instance;

    private Pane canvas;
    private double prevHeight;
    private double prevWidth;
    private boolean prevNew = false;

    private CanvasService() {
    }

    private CanvasService(Pane canvas) {
        this.canvas = canvas;

        addListener();
    }

    public static void init(Pane canvas) {
        if (instance == null) {
            instance = new CanvasService(canvas);
        }
    }

    public static CanvasService getInstance() {
        return instance;
    }

    private void addListener() {
        //监听缩放尺度
        StoreManager.canvasScaleProperty().addListener(((observableValue, number, t1) -> {
            canvas.setScaleX(t1.doubleValue() / 100);
            canvas.setScaleY(t1.doubleValue() / 100);
            if (!prevNew) {
                prevWidth = canvas.getWidth();
                prevHeight = canvas.getHeight();
                prevNew = true;
            }
            canvas.setPrefWidth(prevWidth * (t1.doubleValue() / 100));
            canvas.setPrefHeight(prevHeight * (t1.doubleValue() / 100));
        }));
        //鼠标滚轮缩放
        canvas.setOnScroll(e -> {
            if (e.isControlDown()) {
                int d = StoreManager.getCanvasScale() + (e.getDeltaY() > 0 ? 3 : -3);
                if (d >= 50 && d <= 175) {
                    scale(d);
                    e.consume();
                }
            }
        });
    }

    public void scale(int value) {
        StoreManager.setCanvasScale(value);
    }
}
