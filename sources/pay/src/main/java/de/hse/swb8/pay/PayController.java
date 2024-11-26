package de.hse.swb8.pay;

import de.hse.swb8.pay.core.Records.VehicleType;
import de.hse.swb8.pay.core.RowData;
import de.hse.swb8.pay.core.observer.SimpleObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

public class PayController extends SimpleObservable<PayState> {

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

        PayState state = new PayState(ticketText,-1,false);
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
        notifyObservers(new PayState(ticketText,Float.parseFloat(txtPrice.getText()),false));
    }
}
