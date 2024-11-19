package de.hse.swb8;

import javafx.application.Application;
import javafx.stage.Stage;

public class CheckInMain extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        CheckInCoreSystem coreSystem = new CheckInCoreSystem();
        System.out.println("test");
    }
}
