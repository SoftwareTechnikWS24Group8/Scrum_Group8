package de.hse.swb8.parkingSystem.core;

import de.hse.swb8.parkingSystem.checkin.CheckInMain;
import de.hse.swb8.parkingSystem.core.Records.DataBaseInfo;
import de.hse.swb8.parkingSystem.core.interfaces.Callback;
import de.hse.swb8.parkingSystem.core.observer.Observable;
import de.hse.swb8.parkingSystem.core.observer.Observer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class DataBaseLogin implements Observer<DataBaseInfo> {

    private static final String DIRECTORY_NAME = "ParkingLot";
    private static final String DATABASE_SAVE_FILE_NAME = "Scrum_DataBase";
    private static final String FILE_ENDING = ".json";

    private final String databaseSaveFile;

    Callback callback;

    public DataBaseLogin() {
        String tempDir = System.getProperty("user.home") + File.separator + DIRECTORY_NAME;

        databaseSaveFile = tempDir + File.separator + DATABASE_SAVE_FILE_NAME + FILE_ENDING;
    }


    public void LoginIntoDataBase(Callback onComplete) {
        callback = onComplete;
        //Read file
        DataBaseInfo info = JSONHandler.ReadDataBaseInfo(databaseSaveFile);
        //If file exists
        if (info != null && DataBaseCore.ValidateDataBaseInfo(info)) {
            callback.execute(info);
        } else {
            try {
                StartWindow();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Was not able to open DataBaseLogin Window");
                System.exit(1);
            }
        }
    }

    private DataBaseLoginController controller;

    private void StartWindow() throws IOException {

        System.out.println("Starting window");
        try {
            Stage stage = new Stage();  // Create a new stage (window)
            FXMLLoader fxmlLoader = new FXMLLoader(DataBaseCore.class.getResource("DataBaseLogin.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNIFIED);

            stage.setTitle("DataBase Login");
            stage.setScene(scene);
            stage.show();

            controller = fxmlLoader.getController();
            controller.addObserver(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Observable<DataBaseInfo> observable, DataBaseInfo newValue) {

        if (!DataBaseCore.ValidateDataBaseInfo(newValue)) {
            controller.AppendToLog("Database values were not valid");
            return;
        }
        if (databaseSaveFile == null) {
            System.out.println("ERROR: databasesavefile path was not set but requested");
            System.exit(2);
        }
        JSONHandler.SaveDataBaseInfo(newValue, databaseSaveFile);
        callback.execute(newValue);
    }
}

