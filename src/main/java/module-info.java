module main {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    opens main to javafx.fxml;
    exports main;
}
