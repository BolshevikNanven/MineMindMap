module cn.nanven.mindmap {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens cn.nanven.mindmap to javafx.fxml;
    exports cn.nanven.mindmap;
    exports cn.nanven.mindmap.controller;
    opens cn.nanven.mindmap.controller to javafx.fxml;
    exports cn.nanven.mindmap.common.jackson.serializer to com.fasterxml.jackson.databind;
    exports cn.nanven.mindmap.common.jackson.deserializer to com.fasterxml.jackson.databind;
    exports cn.nanven.mindmap.entity to com.fasterxml.jackson.databind;

}