package de.hse.swb8.parkingSystem.checkin;

import atlantafx.base.theme.PrimerDark;
import de.hse.swb8.parkingSystem.core.DataBaseLogin;
import de.hse.swb8.parkingSystem.core.Records.DataBaseInfo;
import de.hse.swb8.parkingSystem.core.Records.VehicleType;
import de.hse.swb8.parkingSystem.core.interfaces.Callback;
import de.hse.swb8.parkingSystem.core.observer.Observable;
import de.hse.swb8.parkingSystem.core.observer.Observer;
import de.hse.swb8.parkingSystem.core.styles.UiStyler;
import de.hse.swb8.parkingSystem.core.styles.Uistyles;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;


public class CheckInCoreSystem implements Observer<DriveInEvent> {

    CheckInController controller;
    CheckInDB db;

    public static final float DEFAULT_COST = 999f;

    public CheckInCoreSystem() {
        // Start DataBaseLogin
        DataBaseLogin dblogin = new DataBaseLogin();
        Callback callback = this::StartUp;
        dblogin.LoginIntoDataBase(callback);
    }

    private void StartUp(DataBaseInfo info) {
        db = new CheckInDB(info);

        // Start CheckInUI
        try {
            Stage stage = new Stage();  // Create a new stage (window)
            FXMLLoader fxmlLoader = new FXMLLoader(CheckInCoreSystem.class.getResource("CheckIn.fxml"));
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

        PopulateVehicleSelection();
        PopulatePrices();
    }

    private void PopulateVehicleSelection() {
        VehicleType[] vehicleTypes = db.GetVehicleTypes();
        controller.UpdateDropDownSelection(vehicleTypes);
    }

    private void PopulatePrices() {
        VehicleType[] vehicleTypes = db.GetVehicleTypes();

        VehiclePriceList[] priceLists = new VehiclePriceList[vehicleTypes.length];


        for (int i = 0; i < priceLists.length; i++) {
            Dictionary<Float, Float> prices = db.GetPriceList(vehicleTypes[i]);
            int spotMaxAmount = db.CheckMaxAmountSpots(vehicleTypes[i]);
            int spotsUsed = db.CheckSpotUsed(vehicleTypes[i]);
            priceLists[i] = new VehiclePriceList(vehicleTypes[i], prices, spotMaxAmount, spotMaxAmount - spotsUsed);
            // name, Dict<Float,Float> spotAmount, spot unused
        }

        Dictionary<Integer, Float> time_headers = db.GetPriceHeaders();
        List<Float> values = Collections.list(time_headers.elements());
        values.sort(Float::compareTo);
        Float[] sortedArray = values.toArray(new Float[0]);

        controller.PopulatePrices(priceLists, sortedArray);
        controller.PopulateStyleDropDown();
    }

    @Override
    public void update(Observable<DriveInEvent> observable, DriveInEvent selectedDriveInEvent) {

        if(selectedDriveInEvent == DriveInEvent.DriveIn) {
            controller.setTextSize(33);
            VehicleType selectedVehicle = controller.GetSelectedVehicle();
            if (selectedVehicle == null) {
                return;
            }

            if (db.CheckSpotsNotUsed(selectedVehicle) > 0) {
                String ticketID = db.AddParkingVehicle(selectedVehicle);
                controller.SetMessage("Ticket ist: " + ticketID);

                //TODO Start 20 second thread to clear data from gui

                System.out.println("OpenDoor Checkin");

                PopulatePrices();
            } else {
                controller.setTextSize(22);
                controller.SetMessage("Leider gibt es keine Parkmöglichkeit für dieses Auto");
            }
        }else if(selectedDriveInEvent == DriveInEvent.StyleChanged)
        {
            Uistyles style = controller.GetSelectedStyle();
            UiStyler.setUIStyle(style);
        }
    }
}
