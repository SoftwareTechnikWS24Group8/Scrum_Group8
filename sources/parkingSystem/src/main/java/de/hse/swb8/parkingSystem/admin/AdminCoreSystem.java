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
import java.time.LocalDate;
import java.time.LocalDateTime;


public class AdminCoreSystem implements Observer<AdminControllerEvent> {

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
            Scene scene = new Scene(fxmlLoader.load(), 1500, 1010);
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
    
    public double MoneyMade(LocalDate date) {
        double revenue = 0;
        if (db != null) {
            revenue = db.GetRevenueSinceDate(date);
            System.out.println("Total revenue since " + date + ": " + revenue);

        } else {
            System.out.println("Database connection is not initialized.");
        }
        return revenue;
    }


    @Override
    public void update(Observable<AdminControllerEvent> observable, AdminControllerEvent selectedVehicle) {

        switch (selectedVehicle)
        {
            case YearStartChanged -> {
                LocalDate yearstart = controller.GetYearStarted();
                double revenue = MoneyMade(yearstart);
                controller.SetTxtMoneyMade(revenue+"");

            }
        }

    }
}
