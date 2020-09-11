module org.signature {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires java.sql;
    requires java.desktop;
    requires sqlite.jdbc;

    opens com.signature.ui to javafx.fxml;
    exports com.signature.ui;
    exports com.signature.model;

//            --add-exports
//            javafx.base/com.sun.javafx.runtime=com.jfoenix
//            --add-exports
//            javafx.controls/com.sun.javafx.scene.control.behavior=com.jfoenix
//            --add-exports
//            javafx.controls/com.sun.javafx.scene.control=com.jfoenix
//            --add-exports
//            javafx.base/com.sun.javafx.binding=com.jfoenix
//            --add-exports
//            javafx.base/com.sun.javafx.event=com.jfoenix
//            --add-exports
//            javafx.graphics/com.sun.javafx.stage=com.jfoenix
}