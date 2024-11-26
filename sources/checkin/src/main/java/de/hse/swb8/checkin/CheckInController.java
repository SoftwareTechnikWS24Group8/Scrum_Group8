package de.hse.swb8.checkin;

import de.hse.swb8.checkin.core.Enums.VehicleType;
import de.hse.swb8.checkin.core.observer.SimpleObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.util.Arrays;

public class CheckInController extends SimpleObservable<VehicleType> {

    public void initialize() {
    }

    // Helper function to format prices
    private String formatPrices(float[] times, float[] prices) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times.length && i < prices.length; i++) {
            sb.append(String.format("%.1f hrs: €%.2f", times[i], prices[i]));
            if (i < times.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

    @FXML private TableView<RowData> tabTablePrices;

    public void PopulatePrices(VehiclePriceList[] priceLists, Float[] times_in_hours)
    {
        tabTablePrices.getColumns().clear();
        int amountOfColumns = 1+ times_in_hours.length + 2;
        String[] columnHeaders = new String[amountOfColumns];
        columnHeaders[0] = "Fahrzeug Name";
        for (int i = 0; i < times_in_hours.length; i++) {
            columnHeaders[i+1] = times_in_hours[i]+"";
        }

        columnHeaders[amountOfColumns-1] = "Max Slots";
        columnHeaders[amountOfColumns-2] = "Free Slots";

        for (int i = 0; i < columnHeaders.length; i++) {

            TableColumn<RowData, String> column = new TableColumn<>(columnHeaders[i]);
            final int columnIndex = i; // For use in the PropertyValueFactory
            column.setCellValueFactory(data -> data.getValue().getProperty(columnIndex));
            tabTablePrices.getColumns().add(column);
        }

        String[][] data = new String[priceLists.length][amountOfColumns];
        for (int i = 0; i < priceLists.length; i++) {
            data[i][0] = priceLists[i].vehicleType().displayName();
            for (int j = 0; j < times_in_hours.length; j++)
            {
                data[i][j+1] = priceLists[i].priceInEuro().get(times_in_hours[j])+" €";
            }
            data[i][amountOfColumns-2] = priceLists[i].spotUnused()+"";
            data[i][amountOfColumns-1] = priceLists[i].spotAmount()+"";
        }

        ObservableList<RowData> rows = FXCollections.observableArrayList();
        for (int i = 0; i < data.length; i++) {
            rows.add(new RowData(data[i]));
        }
        tabTablePrices.setItems(rows);

    }

    @FXML private TextField txtInfo;

    public void SetMessage(String message)
    {
        txtInfo.setText(message);
    }


    @FXML
    private void OnBtnDriveInPressed(ActionEvent event)
    {
        setChanged();
        notifyObservers(dpdVehicleChoice.getValue());
    }

    @FXML
    private ChoiceBox<VehicleType> dpdVehicleChoice;

    private ObservableList<VehicleType> vehicleTypesList = FXCollections.observableArrayList();

    public void UpdateDropDownSelection(VehicleType[] vehicleTypes) {

        vehicleTypesList = FXCollections.observableArrayList(vehicleTypes);


        // Set the items of the ChoiceBox
        dpdVehicleChoice.setItems(vehicleTypesList);

        // Set the default selected item
        if (vehicleTypes.length > 0) {
            dpdVehicleChoice.setValue(vehicleTypes[0]);
        }

        dpdVehicleChoice.setConverter(new StringConverter<>() {
            @Override
            public String toString(VehicleType vehicleType) {
                return vehicleType.displayName(); // Use displayName as the text
            }

            @Override
            public VehicleType fromString(String string) {
                // Not typically used for ChoiceBox, but must be implemented
                return vehicleTypesList.stream()
                        .filter(vehicleType -> vehicleType.displayName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

    }
}
