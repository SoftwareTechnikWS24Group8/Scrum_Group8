package de.hse.swb8;

import de.hse.swb8.Enums.VehicleType;
import de.hse.swb8.GUI.CheckInController;
import de.hse.swb8.observer.Observable;
import de.hse.swb8.observer.Observer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class CheckInCoreSystem  implements Observer<VehicleType> {

    CheckInController controller;
    CheckInDB db;
    public CheckInCoreSystem()
    {
        // Start DataBaseLogin

        // Give StartUp(message) as callback

        DataBaseLogin dblogin = new DataBaseLogin();

        Callback callback = (message) -> StartUp(message);

        dblogin.LoginIntoDataBase(callback);
    }

    private void StartUp(DataBaseInfo info)
    {
        DataBaseLogin.ValidateDataBaseInfo(info);
        db = new CheckInDB(info);

        // Start CheckInUI
        try {
            Stage stage = new Stage();  // Create a new stage (window)
            FXMLLoader fxmlLoader = new FXMLLoader(CheckInCoreSystem.class.getResource("CheckIn.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
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
        VehicleTypeSpotsInfo[] spots = db.GetParkInfos();
        controller.UpdateTableView(spots);}

    private void PopulatePrices(){
        controller.PopulatePrices(vehicleNames, headers, priceList);
    }

    @Override
    public void update(Observable<VehicleType> observable, VehicleType newValue) {

        if(newValue == null)
        {
            // When button clicked
            VehicleType selectedVehicle = controller.GetCurrentlySelectedVehicle();
            //TODO Validate with db
            if(db.CheckSpotAvailable(selectedVehicle )>= 0)
            {
                String ticketID = db.AddParkingVehicle(selectedVehicle);
                controller.SetMessage("Ticket ist: " + ticketID);

                // Start 20 second thread to clear data from gui

                System.out.println("OpenDoor Checkin");
                return;
            }

            int selectedVehicleSpotsAmount = db.CheckSpotAvailable(selectedVehicle);

            controller.SetMessage("Es gibt noch " + selectedVehicleSpotsAmount + " Park Möglichkeiten ");
        }



    }
}
