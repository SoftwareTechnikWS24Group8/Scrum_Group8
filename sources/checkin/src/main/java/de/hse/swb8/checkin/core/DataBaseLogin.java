package de.hse.swb8.checkin.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hse.swb8.checkin.CheckInCoreSystem;
import de.hse.swb8.checkin.CheckInMain;
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

    private static final String DIRECTORY_NAME = "ParkingLot";
    private static final String DATABASE_SAVE_FILE_NAME = "Scrum_DataBase";

    String databaseSaveFile;
    public DataBaseLogin() {return;}

    public void LoginIntoDataBase(Callback onComplete)
    {
        callback = onComplete;
        String tempDir = System.getProperty("user.home") + File.separator +DIRECTORY_NAME;

        databaseSaveFile = tempDir + File.separator + DATABASE_SAVE_FILE_NAME;

        DataBaseInfo info = JSONHandler.readDataBaseInfo(databaseSaveFile);

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
            FXMLLoader fxmlLoader = new FXMLLoader(CheckInMain.class.getResource("DataBaseLogin.fxml"));

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
        if(databaseSaveFile == null)
        {
            System.out.println("ERROR: databasesavefile path was not set but requested");
            System.exit(2);
        }
        JSONHandler.saveDataBaseInfo(newValue,databaseSaveFile);
        callback.execute(newValue);
    }

    static class JSONHandler
    {
        private static final ObjectMapper objectMapper = new ObjectMapper();

        public static void saveDataBaseInfo(DataBaseInfo info, String filePath) {
            System.out.println("fil" + filePath);
            try {
                File file = new File(filePath + ".json");
                File parentDirectory = file.getParentFile();

                // Check if the parent directory exists; if not, create it
                if (parentDirectory != null && !parentDirectory.exists()) {
                    if (parentDirectory.mkdirs()) {
                        System.out.println("Directories created successfully: " + parentDirectory.getAbsolutePath());
                    } else {
                        System.err.println("Failed to create directories: " + parentDirectory.getAbsolutePath());
                    }
                }

                // Write the JSON file, overriding it if it already exists
                System.out.println("Writing file to " + file.getAbsolutePath());
                objectMapper.writeValue(file, info);
                System.out.println("DataBaseInfo saved to " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error saving DataBaseInfo: " + e.getMessage());
            }
        }

        // Read DataBaseInfo from JSON
        public static DataBaseInfo readDataBaseInfo(String filePath) {
            try {
                System.out.println("ReadFile " + filePath + ".json");

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.findAndRegisterModules(); // Automatically registers support for Java 16+ features

                return objectMapper.readValue(new File(filePath + ".json"), DataBaseInfo.class);
            } catch (IOException e) {
                System.err.println("Error reading DataBaseInfo: " + e.getMessage());
                return null;
            }
        }
    }
}

