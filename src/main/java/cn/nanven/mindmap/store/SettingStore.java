package cn.nanven.mindmap.store;

import cn.nanven.mindmap.entity.SettingEntity;
import cn.nanven.mindmap.service.LayoutService;

public class SettingStore {
    private static SettingEntity setting;
    private static LayoutService layoutService;

    public static SettingEntity getSetting() {
        return setting;
    }

    public static void setSetting(SettingEntity setting) {
        SettingStore.setting = setting;
    }

    public static LayoutService getLayoutService() {
        return layoutService;
    }

    public static void setLayoutService(LayoutService layoutService) {
        SettingStore.layoutService = layoutService;
    }
}
