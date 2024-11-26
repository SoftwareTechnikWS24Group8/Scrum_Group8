package de.hse.swb8.checkout;

import de.hse.swb8.checkout.core.observer.SimpleObservable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CheckOutController extends SimpleObservable<String> {


    public void initialize() {
    }

    @FXML private TextField txtTicketId;

    @FXML
    private void OnBtnReadTicketPressed(ActionEvent ignoredEvent)
    {
        String ticketText = txtTicketId.getText();

        setChanged();
        notifyObservers(ticketText);
    }

    @FXML private TextField txtInfo;

    public void SetInfoText(String info)
    {
        txtInfo.setText(info);
    }
}
