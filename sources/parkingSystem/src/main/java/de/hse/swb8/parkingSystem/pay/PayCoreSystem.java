package de.hse.swb8.parkingSystem.pay;


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
import java.util.Dictionary;
import java.util.Enumeration;


public class PayCoreSystem implements Observer<PayState> {

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
    }



    @Override
    public void update(Observable<PayState> observable, PayState selectedVehicle) {

        if(!selectedVehicle.payed())
        {
            if(db.DoesTicketExistValid(selectedVehicle.ticket_id()))
            {
                controller.SetPriceText(GetPrice(selectedVehicle.ticket_id())+"");
            }else {
                controller.SetInfoText("Dieses Ticket existiert nicht");
            }
        }else {
            db.SetPayed(selectedVehicle.ticket_id(),selectedVehicle.priceInEuro());
        }
    }

    private float GetPrice(String ticket_id)
    {
        VehicleType type = db.GetVehicleTypeFromTicketID(ticket_id);
        Dictionary<Float,Float> prices = db.GetPriceList(type);

        float timeParked = db.getHoursBetweenStampFromTicketAndNow(ticket_id);
        float alreadyPayed = db.GetAlreadyPayedMoney(ticket_id);

        Float applicablePrice = getPriceForTimeParked(prices, timeParked);

        return applicablePrice- alreadyPayed;
    }

    private static Float getPriceForTimeParked(Dictionary<Float, Float> prices, float timeParked) {
        Float highestHour = null;

        Enumeration<Float> keys = prices.keys(); // Get all hours
        while (keys.hasMoreElements()) {
            Float hour = keys.nextElement();
            if (hour <= timeParked && (highestHour == null || hour > highestHour)) {
                highestHour = hour; // Update if it's closer to timeParked but still <= timeParked
            }
        }

        // Return the corresponding price, or null if no valid hour is found
        return highestHour != null ? prices.get(highestHour) : null;
    }
}
