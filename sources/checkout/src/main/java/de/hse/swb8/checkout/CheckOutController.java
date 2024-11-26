package de.hse.swb8.checkout;

import de.hse.swb8.checkout.core.observer.SimpleObservable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CheckOutController extends SimpleObservable<CheckOutState> {

    public static final String MAX_SLOTS_COLUMN_Name = "Alle Parkplätze";
    public static final String USABLE_SLOTS_COLUMN_Name = "Verfügbare Parkplätze";
    public static final String VEHICLE_COLUMN_NAME = "Fahrzeug Name";

    public void initialize() {
    }

    @FXML private TextField txtTicketId;

    @FXML
    private void OnBtnReadTicketPressed(ActionEvent ignoredEvent)
    {
        String ticketText = txtTicketId.getText();

        CheckOutState state = new CheckOutState(ticketText,-1,false);
        setChanged();
        notifyObservers(state);
    }

    @FXML private TextField txtInfo;

    public void SetInfoText(String info)
    {
        txtInfo.setText(info);
    }

    @FXML private TextField txtPrice;

    public void SetPriceText(String price)
    {
        txtPrice.setText(price);
    }

    @FXML void OnBtnPayPressed(ActionEvent ignoredEvent)
    {
        setChanged();
        String ticketText = txtTicketId.getText();
        notifyObservers(new CheckOutState(ticketText,Float.parseFloat(txtPrice.getText()),true));
    }
}
