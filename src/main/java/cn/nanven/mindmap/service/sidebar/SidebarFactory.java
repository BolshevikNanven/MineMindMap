package cn.nanven.mindmap.service.sidebar;

import cn.nanven.mindmap.service.SidebarService;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class SidebarFactory {
    private static SidebarFactory instance;
    private AnchorPane canvas;

    private SidebarFactory() {

    }

    private SidebarFactory(AnchorPane canvas) {
        this.canvas = canvas;
    }

    public static void init(AnchorPane canvas) {
        if (null == instance) {
            instance = new SidebarFactory(canvas);
        }
    }

    public static SidebarFactory getInstance() {
        return instance;
    }

    public SidebarService getService(String type) {
        switch (type) {
            case "outline" -> {
                return new OutlineService();
            }
            case "design" -> {
                return new DesignService();
            }
            default -> {
                return null;
            }
        }
    }
}
