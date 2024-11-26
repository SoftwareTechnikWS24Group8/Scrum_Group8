package de.hse.swb8.checkin.checkout;

import javafx.application.Application;
import javafx.stage.Stage;

public class CheckOutMain extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        CheckOutCoreSystem coreSystem = new CheckOutCoreSystem();
    }
}
