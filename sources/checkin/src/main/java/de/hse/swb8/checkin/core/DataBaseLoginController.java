package de.hse.swb8.checkin.core;

import de.hse.swb8.checkin.core.observer.SimpleObservable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DataBaseLoginController extends SimpleObservable<DataBaseInfo> {

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

         String dataBaseURL = "jdbc:postgresql://46.223.191.31:5432/DataBase";
         String dataBaseUser = "dbuserDefault";
         String dataBasePass = "dbpasswordwaterfall8";

        DataBaseInfo dbinfo = new DataBaseInfo(dataBaseURL,dataBaseUser,dataBasePass);

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
