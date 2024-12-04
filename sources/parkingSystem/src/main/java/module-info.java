module de.hse.swb8.parkingSystem {
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
    requires atlantafx.base;

    //checkout
    opens de.hse.swb8.parkingSystem.checkout;

    exports de.hse.swb8.parkingSystem.checkout;

    //checkin
    opens de.hse.swb8.parkingSystem.checkin to javafx.fxml;

    exports de.hse.swb8.parkingSystem.checkin;

    //pay
    opens de.hse.swb8.parkingSystem.pay to javafx.fxml;

    exports de.hse.swb8.parkingSystem.pay;

    //admin

    opens de.hse.swb8.parkingSystem.admin to javafx.fxml;

    exports de.hse.swb8.parkingSystem.admin;

    //Core
    // Allow both Jackson and JavaFX FXML to access this package
    opens de.hse.swb8.parkingSystem.core to com.fasterxml.jackson.databind, javafx.fxml;
    opens de.hse.swb8.parkingSystem.core.Records to com.fasterxml.jackson.databind, javafx.fxml;
    opens de.hse.swb8.parkingSystem.core.interfaces to com.fasterxml.jackson.databind, javafx.fxml;

    exports de.hse.swb8.parkingSystem.core;
    exports de.hse.swb8.parkingSystem.core.Records;
    exports de.hse.swb8.parkingSystem.core.interfaces;


}
