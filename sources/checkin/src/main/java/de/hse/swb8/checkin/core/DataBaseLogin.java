package de.hse.swb8.checkin.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hse.swb8.checkin.CheckInCoreSystem;
import de.hse.swb8.checkin.core.observer.Observable;
import de.hse.swb8.checkin.core.observer.Observer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseLogin implements Observer<DataBaseInfo> {

    Callback callback;

    private static final String DATABASE_SAVE_FILE_NAME = "Scrum_DataBase";

    public DataBaseLogin() {return;}

    public void LoginIntoDataBase(Callback onComplete)
    {
        callback = onComplete;
        String tempDir = System.getProperty("java.io.tmpdir");
        String filePath = tempDir + File.separator + DATABASE_SAVE_FILE_NAME;

        DataBaseInfo info = JSONHandler.readDataBaseInfo(filePath);

        if(info != null && ValidateDataBaseInfo(info))
        {
            callback.execute(info);
            return;
        }

        System.out.println("No old valid dataBaseInfo found");

        try {
            startWindow();
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    public static boolean ValidateDataBaseInfo(final @NotNull DataBaseInfo info) {
        // Load the database driver if required (for older JDBC versions).
        // For newer JDBC versions, the driver is auto-registered.
        System.out.println("Validating dataBaseInfo " + info.toString());
        try (Connection connection = DriverManager.getConnection(info.url(), info.userName(), info.password())) {
            // If we reach here, the connection is successful
            System.out.println("Connection successful!");
            return true;
        } catch (SQLException e) {
            // Handle specific SQL exceptions
            if (e.getSQLState().equals("28000")) { // Invalid authorization
                System.out.println("Invalid credentials: " + e.getMessage());
            } else {
                System.out.println("Database connection error: " + e.getMessage());
            }
            return false;
        }
    }

    private DataBaseLoginController controller;

    private void startWindow() throws IOException {

        System.out.println("Starting window");
        try {
            Stage stage = new Stage();  // Create a new stage (window)
            FXMLLoader fxmlLoader = new FXMLLoader(CheckInCoreSystem.class.getResource("DataBaseLogin.fxml"));

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

        //checks
        System.out.println(newValue);
        if(!ValidateDataBaseInfo(newValue))
        {
            controller.setErrorText("DataBaseNotValid");
            return;
        }
        String tempDir = System.getProperty("java.io.tmpdir");
        String filePath = tempDir + File.separator + DATABASE_SAVE_FILE_NAME;
        JSONHandler.saveDataBaseInfo(newValue,filePath);
        callback.execute(newValue);
    }

    static class JSONHandler
    {
        private static final ObjectMapper objectMapper = new ObjectMapper();

        public static void saveDataBaseInfo(DataBaseInfo info, String filePath) {
            try {
                objectMapper.writeValue(new File(filePath), info);
                System.out.println("DataBaseInfo saved to " + filePath + ".json");
            } catch (IOException e) {
                System.err.println("Error saving DataBaseInfo: " + e.getMessage());
            }
        }

        // Read DataBaseInfo from JSON
        public static DataBaseInfo readDataBaseInfo(String filePath) {
            try {
                return objectMapper.readValue(new File(filePath + ".json"), DataBaseInfo.class);
            } catch (IOException e) {
                System.err.println("Error reading DataBaseInfo: " + e.getMessage());
                return null;
            }
        }
    }
}
