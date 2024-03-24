package cn.nanven.mindmap.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class SidebarController {
    private SimpleIntegerProperty selectedTab = new SimpleIntegerProperty(0);
    private HBox sideBarTab;
    private BorderPane sidebar;
    private Button foldBtn;
    private Label title;

    public SidebarController(HBox sideBarTab, BorderPane sidebar) {
        this.sideBarTab = sideBarTab;
        this.sidebar = sidebar;
        this.foldBtn = (Button) sidebar.lookup("#sidebar-fold-btn");
        this.title = (Label) sidebar.lookup("#sidebar-title");

        addListener();
        updateState(selectedTab.get());
    }

    private void addListener() {
        for (int i = 0; i < sideBarTab.getChildren().size(); i++) {
            Button btn = (Button) sideBarTab.getChildren().get(i);
            int finalI = i;
            btn.setOnAction(e -> {
                if (selectedTab.get() != finalI) {
                    selectedTab.set(finalI);
                    title.setText(btn.getText());
                } else selectedTab.set(-1);
            });
        }
        selectedTab.addListener((e) -> {
            if (selectedTab.get() == -1) {
                sidebar.setPrefWidth(0);
            } else sidebar.setPrefWidth(294);
            updateState(selectedTab.get());
        });
        foldBtn.setOnAction(e -> {
            selectedTab.set(-1);
        });
    }

    private void updateState(int index) {
        for (int i = 0; i < sideBarTab.getChildren().size(); i++) {
            if (index == i) {
                sideBarTab.getChildren().get(i).getStyleClass().add("active");
            } else sideBarTab.getChildren().get(i).getStyleClass().remove("active");
        }
    }
}
