package de.hse.swb8;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hse.swb8.observer.Observable;
import de.hse.swb8.observer.Observer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseLogin implements Observer<String> {

    Callback callback;

    private static final String DATABASE_SAVE_FILE_NAME = "Scrum_DataBase";

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

        System.out.println("NO valid dataBaseInfo found");

        try {
            startWindow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean ValidateDataBaseInfo(DataBaseInfo info)
    {
        // Load the database driver if required (for older JDBC versions).
        // For newer JDBC versions, the driver is auto-registered.
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
        try {
            Stage stage = new Stage();  // Create a new stage (window)
            FXMLLoader fxmlLoader = new FXMLLoader(DataBaseLogin.class.getResource("DataBaseLogin.fxml"));
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
    public void update(Observable<String> observable, String newValue) {

        //checks
        System.out.println(newValue);
        DataBaseInfo info = new DataBaseInfo("","","");
        if(!ValidateDataBaseInfo(info))
        {
            controller.setErrorText("DataBaseNotValid");
            return;
        }
        String tempDir = System.getProperty("java.io.tmpdir");
        String filePath = tempDir + File.separator + DATABASE_SAVE_FILE_NAME;
        JSONHandler.saveDataBaseInfo(info,filePath);
        callback.execute(info);
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

