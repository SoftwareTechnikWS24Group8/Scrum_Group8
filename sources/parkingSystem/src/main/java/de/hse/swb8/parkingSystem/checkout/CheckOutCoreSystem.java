package de.hse.swb8.parkingSystem.checkout;

import de.hse.swb8.parkingSystem.core.DataBaseLogin;
import de.hse.swb8.parkingSystem.core.Records.DataBaseInfo;
import de.hse.swb8.parkingSystem.core.interfaces.Callback;
import de.hse.swb8.parkingSystem.core.observer.Observable;
import de.hse.swb8.parkingSystem.core.observer.Observer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class CheckOutCoreSystem implements Observer<String> {

    private static final float TIME_AFTER_PAYMENT = 0.17f; // ~10 Minunten

    CheckOutController controller;
    CheckOutDB db;

    public static float getTIME_AFTER_PAYMENT(){
        return TIME_AFTER_PAYMENT;
    }

    public CheckOutCoreSystem() {
        // Start DataBaseLogin
        DataBaseLogin dblogin = new DataBaseLogin();
        Callback callback = this::StartUp;
        dblogin.LoginIntoDataBase(callback);
    }

    private void StartUp(DataBaseInfo info) {
        db = new CheckOutDB(info);

        // Start payUI
        try {
            Stage stage = new Stage();  // Create a new stage (window)
            FXMLLoader fxmlLoader = new FXMLLoader(CheckOutCoreSystem.class.getResource("CheckOut.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 750, 550);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNIFIED);
            stage.setTitle("CheckOut Window");
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
    public void update(Observable<String> observable, String ticketId) {

        if (db.DoesTicketExistValid(ticketId)) {
            if (db.HasTicketBeenPayed(ticketId)) {
                float timer_after_payment = db.getHoursBetweenStampFromTicketAndNow(ticketId);

                if (timer_after_payment > TIME_AFTER_PAYMENT) {
                    controller.SetInfoText("Zeit überschreitung zahle erneut");
                } else {
                    controller.SetInfoText("Gute Fahrt!");
                    System.out.println("Schranke Auf _------------");
                    db.SetCheckoutTime(ticketId);

                    float already_payed = db.GetAlreadyPayedMoney(ticketId);
                    System.out.println(already_payed);
                    db.AddRowToPayedList(ticketId, already_payed);
                }
            } else {
                controller.SetInfoText("Dieses Ticket wurde nicht bezahlt");
            }
        } else {
            controller.SetInfoText("Dieses Ticket exisitert nicht");
        }

    }
}
