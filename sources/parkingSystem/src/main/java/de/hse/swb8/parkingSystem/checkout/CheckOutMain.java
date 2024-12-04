package de.hse.swb8.parkingSystem.checkout;

import atlantafx.base.theme.CupertinoDark;
import javafx.application.Application;
import javafx.stage.Stage;

public class CheckOutMain extends Application {

    public static void main(String[] args) {
        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        CheckOutCoreSystem coreSystem = new CheckOutCoreSystem();
    }
}
