package de.hse.swb8;

import de.hse.swb8.observer.SimpleObservable;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DataBaseLoginController extends SimpleObservable<String> {

    private static final String SEPERATOR = "--AA__";
    @FXML
    private TextField txtUser;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtUrl;
    @FXML
    private Label lblErrorMessage;

    public void setErrorText(String msg) {
        lblErrorMessage.setText(msg);
    }

    @FXML
    void OnLoginButtonPressed(ActionEvent event) {

        StringBuilder sb = new StringBuilder();
        sb.append(txtUser.getText());
        sb.append(txtPassword.getText());
        sb.append(txtUrl.getText());
        if(sb.toString().contains(SEPERATOR))
        {
            lblErrorMessage.setText("No entry can contain: " + SEPERATOR);
            return;
        }

        sb = new StringBuilder();
        sb.append(txtUser.getText());
        sb.append(SEPERATOR);
        sb.append(txtPassword.getText());
        sb.append(SEPERATOR);
        sb.append(txtUrl.getText());

        this.setChanged();
        this.notifyObservers(sb.toString());

    }

    @FXML
    void OnCancelButtonPressed(ActionEvent event) {
        System.exit(0);
    }
}
