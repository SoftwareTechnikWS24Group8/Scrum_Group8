package de.hse.swb8.parkingSystem.admin;

import de.hse.swb8.parkingSystem.core.DataBaseLogin;
import de.hse.swb8.parkingSystem.core.Records.DataBaseInfo;
import de.hse.swb8.parkingSystem.core.Records.VehicleType;
import de.hse.swb8.parkingSystem.core.interfaces.Callback;
import de.hse.swb8.parkingSystem.core.observer.Observable;
import de.hse.swb8.parkingSystem.core.observer.Observer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class AdminCoreSystem implements Observer<VehicleType> {

    AdminController controller;
    AdminDB db;

    public AdminCoreSystem() {
        // Start DataBaseLogin
        DataBaseLogin dblogin = new DataBaseLogin();
        Callback callback = this::StartUp;
        dblogin.LoginIntoDataBase(callback);
    }

    private void StartUp(DataBaseInfo info) {
        db = new AdminDB(info);

        // Start CheckInUI
        try {
            Stage stage = new Stage();  // Create a new stage (window)
            FXMLLoader fxmlLoader = new FXMLLoader(AdminCoreSystem.class.getResource("Admin.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 750, 550);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNIFIED);
            stage.setTitle("Check In");
            stage.setScene(scene);
            stage.show();

            controller = fxmlLoader.getController();
            controller.addObserver(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Observable<VehicleType> observable, VehicleType selectedVehicle) {

    }
}
