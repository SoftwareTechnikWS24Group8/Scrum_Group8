package de.hse.swb8.parkingSystem.pay;

import atlantafx.base.theme.CupertinoDark;
import javafx.application.Application;
import javafx.stage.Stage;

public class PayMain extends Application {

    public static void main(String[] args) {
        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        PayCoreSystem coreSystem = new PayCoreSystem();
    }
}
