package de.hse.swb8.checkin;

import de.hse.swb8.checkin.core.Enums.VehicleType;
import de.hse.swb8.checkin.core.observer.SimpleObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.util.Arrays;
import java.util.List;

public class CheckInController extends SimpleObservable<VehicleType> {

    public CheckInController() {
        //tableVehicleSelected
    }
    @FXML
    private Label lblMessage;

    @FXML private TableView tableVehicleSelected; //TODO make real

    private ObservableList<VehicleTypeSpotsInfo> selectedVehicles;
    public void UpdateTableView(VehicleTypeSpotsInfo[] spots){
        //TODO make a obeservable list to insert into the table
        int selectedIndex = tableVehicleSelected.getSelectionModel().getSelectedIndex();
        selectedVehicles.clear();
        selectedVehicles.addAll(spots);
        tableVehicleSelected.getSelectionModel().select(selectedIndex);
    }

    public VehicleType GetCurrentlySelectedVehicle()
    {
        //TODO implement
        return new VehicleType(1,"","");
    }

    @FXML private TableView tablePrices; //TODO make real

    public void PopulatePrices(String[] vehicleNames,List<String> headers, List<List<String>> priceList)
    {
        //TODO Set Table rows with the data
    }

    public void SetMessage(String message)
    {
        lblMessage.setText(message);
    }
    @FXML
    private void OnRadioButtonGroupUpdated()
    {
        /* TODO sent
        get selected vehicle type
         */

        VehicleType selectedVehicle = new VehicleType(1,"","");
        setChanged();
        notifyObservers(selectedVehicle);
    }

    @FXML
    private void OnBtnDriveInPressed(ActionEvent event)
    {
        VehicleType selectedVehicle = new VehicleType(1,"",""); //TODO make real
        notifyObservers();
    }

    @FXML
    private ChoiceBox<String> dpdVehicleChoice;

    public void UpdateDropDownSelection(VehicleType[] vehicleTypes) {
        // Extract display names from the array of VehicleType
        ObservableList<String> displayNames = FXCollections.observableArrayList(
                Arrays.stream(vehicleTypes)
                        .map(VehicleType::displayName) // Extract displayName from each VehicleType
                        .toList()                      // Convert the Stream to a List
        );

        // Set the items of the ChoiceBox
        dpdVehicleChoice.setItems(displayNames);
    }
}
