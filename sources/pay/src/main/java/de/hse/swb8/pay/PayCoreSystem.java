package de.hse.swb8.pay;

import de.hse.swb8.pay.core.DataBaseLogin;
import de.hse.swb8.pay.core.Records.DataBaseInfo;
import de.hse.swb8.pay.core.Records.VehicleType;
import de.hse.swb8.pay.core.interfaces.Callback;
import de.hse.swb8.pay.core.observer.Observable;
import de.hse.swb8.pay.core.observer.Observer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;


public class PayCoreSystem implements Observer<VehicleType> {

    PayController controller;
    PayDB db;

    public PayCoreSystem()
    {
        // Start DataBaseLogin
        DataBaseLogin dblogin = new DataBaseLogin();
        Callback callback = this::StartUp;
        dblogin.LoginIntoDataBase(callback);
    }

    private void StartUp(DataBaseInfo info)
    {
        db = new PayDB(info);

        // Start payUI
        try {
            Stage stage = new Stage();  // Create a new stage (window)
            FXMLLoader fxmlLoader = new FXMLLoader(PayCoreSystem.class.getResource("Pay.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 750 , 550);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNIFIED);
            stage.setTitle("Pay Window");
            stage.setScene(scene);
            stage.show();

            controller = fxmlLoader.getController();
            controller.addObserver(this);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        PopulateVehicleSelection();
        PopulatePrices();
    }

    private void PopulateVehicleSelection() {
        VehicleType[] vehicleTypes = db.GetVehicleTypes();
        controller.UpdateDropDownSelection(vehicleTypes);
        }

    private void PopulatePrices(){
        VehicleType[] vehicleTypes = db.GetVehicleTypes();

        VehiclePriceList[] priceLists = new VehiclePriceList[vehicleTypes.length];


        for(int i = 0; i < priceLists.length; i++)
        {
            Dictionary<Float, Float> prices = db.GetPriceList(vehicleTypes[i]);
            int spotMaxAmount = db.CheckMaxAmountSpots(vehicleTypes[i]);
            int spotsUsed = db.CheckSpotUsed(vehicleTypes[i]);
            priceLists[i] = new VehiclePriceList(vehicleTypes[i],prices,spotMaxAmount,spotMaxAmount-spotsUsed);
            // name, Dict<Float,Float> spotAmount, spot unused
        }

        Dictionary<Integer, Float> time_headers = db.GetPriceHeaders();
        List<Float> values = Collections.list(time_headers.elements());
        values.sort(Float::compareTo);
        Float[] sortedArray = values.toArray(new Float[0]);

        controller.PopulatePrices(priceLists,sortedArray);
    }

    @Override
    public void update(Observable<VehicleType> observable, VehicleType selectedVehicle) {

        if(selectedVehicle == null)
        {return;}

        if(db.CheckSpotsNotUsed(selectedVehicle )> 0)
        {
            String ticketID = db.AddParkingVehicle(selectedVehicle);
            controller.SetMessage("Ticket ist: " + ticketID);

            //TODO Start 20 second thread to clear data from gui

            System.out.println("OpenDoor pay");

            PopulatePrices();
        }else {
            controller.SetMessage("Leider gibt es keine Parkmöglichkeit für dieses Auto");
        }
    }
}
