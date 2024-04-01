module cn.nanven.mindmap {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens cn.nanven.mindmap to javafx.fxml;
    exports cn.nanven.mindmap;
    exports cn.nanven.mindmap.controller;
    opens cn.nanven.mindmap.controller to javafx.fxml;
}