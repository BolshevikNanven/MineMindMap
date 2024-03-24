package cn.nanven.mindmap;

import cn.nanven.mindmap.service.WindowService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-view.fxml"));
        Parent root = fxmlLoader.load();
        root.setStyle("-fx-background-radius: 4");

        Scene scene = new Scene(root, 1080, 680);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("MindMap");
        stage.setScene(scene);
        stage.show();

        WindowService.init(root,stage);
    }

    public static void main(String[] args) {
        launch();
    }
}