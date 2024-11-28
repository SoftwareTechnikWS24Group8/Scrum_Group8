package de.hse.swb8.parkingSystem.pay;

import de.hse.swb8.parkingSystem.core.observer.SimpleObservable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PayController extends SimpleObservable<PayState> {

    public void initialize() {
    }

    @FXML
    private TextField txtTicketId;

    @FXML
    private Label durationLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private void OnBtnReadTicketPressed(ActionEvent ignoredEvent) {
        String ticketText = txtTicketId.getText();

        PayState state = new PayState(ticketText, -1, false);
        setChanged();
        notifyObservers(state);
    }

    @FXML
    private TextField txtInfo;

    public void SetInfoText(String info) {
        txtInfo.setText(info);
    }

    @FXML
    private TextField txtPrice;

    public void setTimeLabel(String time) {
        timeLabel.setText(time);
    }

    public void setDurationLabel(String duration) {
        durationLabel.setText(duration);
    }

    public void SetPriceText(String price) {
        txtPrice.setText(price);
    }

    @FXML
    void OnBtnPayPressed(ActionEvent ignoredEvent) {
        setChanged();
        String ticketText = txtTicketId.getText();
        String priceText = txtPrice.getText().replace("€", "").trim();
        notifyObservers(new PayState(ticketText, Float.parseFloat(priceText), true));
    }
}
