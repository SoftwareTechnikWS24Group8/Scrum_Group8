module de.hse.swb8.checkin {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires annotations;

    opens de.hse.swb8.checkin to javafx.fxml;
    opens de.hse.swb8.checkin.core to javafx.fxml;
    exports de.hse.swb8.checkin;
    exports de.hse.swb8.checkin.core;
}