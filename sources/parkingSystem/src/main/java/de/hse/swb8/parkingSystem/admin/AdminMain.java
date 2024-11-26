package de.hse.swb8.parkingSystem.admin;

import javafx.application.Application;
import javafx.stage.Stage;

public class AdminMain extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        AdminCoreSystem coreSystem = new AdminCoreSystem();
    }
}
