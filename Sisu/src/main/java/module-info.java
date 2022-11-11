module fi.tuni.prog3.sisu {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires transitive javafx.media;
    requires javafx.base;
    requires transitive javafx.graphics;
    requires transitive javafx.web;

    opens fi.tuni.prog3.sisu to javafx.fxml, javafx.graphics, com.google.gson;

    exports fi.tuni.prog3.sisu;
}
