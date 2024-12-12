package de.hse.swb8.parkingSystem.checkin;

import atlantafx.base.theme.*;
import de.hse.swb8.parkingSystem.core.styles.UiStyler;
import de.hse.swb8.parkingSystem.core.styles.Uistyles;
import javafx.application.Application;
import javafx.stage.Stage;

public class CheckInMain extends Application {

    /*
    cupertionoDark
    cupertionoLight
    Dracula
    NordDark
    NordLight
    PrimerDark
    PrimerLight
     */
    public static void main(String[] args) {
        UiStyler.setUIStyle(Uistyles.cupertionoDark);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        CheckInCoreSystem coreSystem = new CheckInCoreSystem();
    }
}
