package cn.nanven.mindmap.store;

import cn.nanven.mindmap.entity.SettingEntity;
import cn.nanven.mindmap.service.LayoutService;
import cn.nanven.mindmap.service.layout.LayoutFactory;

public class SettingStore {
    static {
        setting = new SettingEntity();
        setLayout("RightTreeLayout");
        setLine("TwoPolyLine");
        layoutService= LayoutFactory.getInstance().getService("RightTreeLayout");
    }

    private static final SettingEntity setting;
    private static LayoutService layoutService;
    public static String getLayout() {
        return setting.getLayout();
    }

    public static void setLayout(String layout) {
        setting.setLayout(layout);
    }

    public static String getLine() {
        return setting.getLine();
    }

    public static void setLine(String line) {
        setting.setLine(line);
    }

    public static LayoutService getLayoutService() {
        return layoutService;
    }

    public static void setLayoutService(LayoutService layoutService) {
        SettingStore.layoutService = layoutService;
    }
}
