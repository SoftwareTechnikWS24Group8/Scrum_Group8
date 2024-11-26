package de.hse.swb8.checkout.core;

import de.hse.swb8.checkout.core.Records.DataBaseInfo;
import de.hse.swb8.checkout.core.observer.SimpleObservable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DataBaseLoginController extends SimpleObservable<DataBaseInfo> {

    @FXML
    private TextField txtUser;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtUrl;
    @FXML
    private TextArea txaLog;

    public void AppendToLog(String msg) {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timestamp = time.format(formatter);
        txaLog.appendText(timestamp +": " +msg);
    }

    @FXML
    void OnLoginButtonPressed(ActionEvent ignoredEvent) {

        DataBaseInfo dbInfo = new DataBaseInfo(txtUrl.getText(),txtUser.getText(),txtPassword.getText());
        this.setChanged();
        this.notifyObservers(dbInfo);
    }

    @FXML
    void OnCancelButtonPressed(ActionEvent ignoredEvent) {
        System.out.println("Application was closed with Cancel Button in DataBaseLogin");
        System.exit(0);
    }
}
