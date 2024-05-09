package cn.nanven.mindmap.service;

import cn.nanven.mindmap.dao.NodeDao;
import cn.nanven.mindmap.entity.NodeEntity;
import cn.nanven.mindmap.store.SystemStore;
import cn.nanven.mindmap.store.ThreadsPool;
import cn.nanven.mindmap.util.AlgorithmUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;

public class FileService {
    private static FileService instance;
    private Stage stage;
    private Scene scene;
    private ObjectMapper objectMapper;
    private MenuButton menuButton;

    private FileService() {
    }

    private FileService(Parent root, Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
        this.objectMapper = new ObjectMapper();
        this.menuButton = (MenuButton) root.lookup("#menu");

        addListener();
    }

    public static void init(Parent root, Stage stage, Scene scene) {
        if (null == instance) {
            instance = new FileService(root, stage, scene);
        }
    }

    public static FileService getInstance() {
        return instance;
    }

    private void addListener() {
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.isControlDown()) {
                if (keyEvent.getCode() == KeyCode.S) {
                    handleSave();
                    keyEvent.consume();
                }
            }
        });
        menuButton.getItems().forEach(menuItem -> {
            switch (menuItem.getId()) {
                case "save-as-file-menu" -> menuItem.setOnAction(actionEvent -> writeFile());
                case "save-file-menu" -> menuItem.setOnAction(actionEvent -> handleSave());
                case "new-file-menu" -> menuItem.setOnAction(actionEvent -> newFile());
                case "open-file-menu" -> menuItem.setOnAction(actionEvent -> readFile());
            }
        });
    }

    private void handleSave() {
        if (SystemStore.getFile() == null) {
            writeFile();
        } else {
            writeFile(SystemStore.getFile());
        }
    }

    public void writeFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存到");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("json", "*.json"));
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            return;
        }

        ThreadsPool.run(() -> {
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException();
                }

                FileWriter fileWriter = new FileWriter(file);
                objectMapper.writer().writeValue(fileWriter, SystemStore.getRootNodeList());

                fileWriter.close();
                if (SystemStore.getFile() == null) {
                    SystemStore.setFile(file);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void writeFile(File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            objectMapper.writer().writeValue(fileWriter, SystemStore.getRootNodeList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("json", "*.json"));
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }

        ThreadsPool.run(() -> {
            try (FileReader fileReader = new FileReader(file)) {
                List<NodeEntity> list = objectMapper.readValue(fileReader, new TypeReference<>() {
                });
                SystemStore.getRootNodeList().forEach(node -> {
                    Platform.runLater(() -> NodeDao.deleteNode(node));
                });
                SystemStore.setFile(file);
                for (NodeEntity root : list) {
                    SystemStore.getRootNodeList().add(root);
                    //序列化时无parent参数，需手动添加
                    AlgorithmUtil.headMapNode(root, (parent, node) -> {
                        node.setParent(parent);
                        node.setDisabled(true);
                    });
                }
                Platform.runLater(() -> NodeService.getInstance().renderNodeTree());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }

    public void newFile() {

    }
}
