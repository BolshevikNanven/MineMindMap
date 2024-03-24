package cn.nanven.mindmap.service.sidebar;

import cn.nanven.mindmap.service.LayoutService;
import cn.nanven.mindmap.service.layout.MindMapLayout;
import javafx.scene.layout.Pane;

public class SidebarFactory {
    private static SidebarFactory instance;
    private Pane canvas;

    private SidebarFactory() {

    }

    private SidebarFactory(Pane canvas) {
        this.canvas = canvas;
    }

    public static void init(Pane canvas) {
        if (null == instance) {
            instance = new SidebarFactory(canvas);
        }
    }

    public static SidebarFactory getInstance() {
        return instance;
    }

    public LayoutService getService(String type) {
        switch (type) {
            default -> {
                return new MindMapLayout(canvas);
            }
        }
    }
}