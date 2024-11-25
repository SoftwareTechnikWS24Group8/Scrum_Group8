module de.hse.swb8.checkin {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.fasterxml.jackson.databind;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires annotations;

    // Allow both Jackson and JavaFX FXML to access this package
    opens de.hse.swb8.checkin.core to com.fasterxml.jackson.databind, javafx.fxml;

    // FXML loader needs reflective access to this package as well
    opens de.hse.swb8.checkin to javafx.fxml;

    exports de.hse.swb8.checkin;
    exports de.hse.swb8.checkin.core;
}
