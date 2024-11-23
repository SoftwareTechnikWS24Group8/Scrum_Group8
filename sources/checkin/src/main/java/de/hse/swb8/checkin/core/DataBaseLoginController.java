package de.hse.swb8.checkin.core;

import de.hse.swb8.checkin.core.observer.SimpleObservable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DataBaseLoginController extends SimpleObservable<DataBaseInfo> {

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

        DataBaseInfo dbinfo = new DataBaseInfo(txtUrl.getText(),txtUser.getText(),txtPassword.getText());

        this.setChanged();
        this.notifyObservers(dbinfo);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    void OnCancelButtonPressed(ActionEvent event) {
        System.exit(0);
    }
}