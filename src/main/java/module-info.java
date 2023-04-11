module main.chris482 {
    requires javafx.controls;
    requires javafx.fxml;


    opens models to javafx.fxml;
    exports models;
    exports controller;
    opens controller to javafx.fxml;
    exports starter;
    opens starter to javafx.fxml;
}