package cn.nanven.mindmap.service;

import cn.nanven.mindmap.entity.NodeEntity;
import cn.nanven.mindmap.store.StoreManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class FileService {
    private static FileService instance;
    private Stage stage;
    private ObjectMapper objectMapper;
    private MenuBar menuBar;
    private File file;

    private FileService() {
    }

    private FileService(Stage stage, Parent root) {
        this.stage = stage;
        this.objectMapper = new ObjectMapper();
        this.menuBar = (MenuBar) root.lookup("#menu-bar");

        addListener();
    }

    public static void init(Stage stage, Parent root) {
        if (null == instance) {
            instance = new FileService(stage, root);
        }
    }

    public static FileService getInstance() {
        return instance;
    }

    private void addListener() {
        menuBar.getMenus().get(0).getItems().forEach(menuItem -> {
            switch (menuItem.getId()) {
                case "save-as-file-menu" -> {
                    menuItem.setOnAction(actionEvent -> writeFile());
                }
            }
        });
    }

    public void writeFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存到");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json", "*.json"));
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            return;
        }
        file.deleteOnExit();
        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            objectMapper.writer().writeValue(fileWriter, StoreManager.getRootNodeList());

            if (this.file == null) {
                this.file = file;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
