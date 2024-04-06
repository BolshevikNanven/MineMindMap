package cn.nanven.mindmap.service;

import cn.nanven.mindmap.store.StoreManager;
import javafx.scene.layout.Pane;

public class CanvasService {
    private static CanvasService instance;

    private Pane canvas;

    private CanvasService() {
    }

    private CanvasService(Pane canvas) {
        this.canvas = canvas;

        StoreManager.canvasScaleProperty().addListener(((observableValue, number, t1) -> {
            canvas.setScaleX((int) t1 / 100.0);
            canvas.setScaleY((int) t1 / 100.0);
        }));
    }

    public static void init(Pane canvas) {
        if (instance == null) {
            instance = new CanvasService(canvas);
        }
    }

    public static CanvasService getInstance() {
        return instance;
    }

    public void scale(int value) {
        StoreManager.setCanvasScale(value);
    }
}
