package de.hse.swb8.checkin;

import de.hse.swb8.checkin.core.Enums.VehicleType;
import de.hse.swb8.checkin.core.observer.SimpleObservable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

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
        return VehicleType.C_Class;
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

        VehicleType selectedVehicle = VehicleType.Default;
        setChanged();
        notifyObservers(selectedVehicle);
    }

    @FXML
    private void OnBuyTicketPressed(ActionEvent event)
    {
        VehicleType selectedVehicle = VehicleType.Default; //TODO make real
        notifyObservers();
    }

}
