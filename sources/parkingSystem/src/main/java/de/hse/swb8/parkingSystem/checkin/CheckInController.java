package de.hse.swb8.parkingSystem.checkin;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.Dracula;
import de.hse.swb8.parkingSystem.core.Records.VehicleType;
import de.hse.swb8.parkingSystem.core.RowData;
import de.hse.swb8.parkingSystem.core.observer.SimpleObservable;
import de.hse.swb8.parkingSystem.core.styles.Uistyles;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class CheckInController extends SimpleObservable<DriveInEvent> {

    public static final String MAX_SLOTS_COLUMN_Name = "Alle Parkplätze";
    public static final String USABLE_SLOTS_COLUMN_Name = "Verfügbare Parkplätze";
    public static final String VEHICLE_COLUMN_NAME = "Fahrzeug Name";
    public static final String CURRENCY_SIGN = "€";

    public void initialize() {
    }

    @FXML
    private TableView<RowData> tabTablePrices;


    public void PopulatePrices(VehiclePriceList[] priceLists, Float[] times_in_hours) {
        tabTablePrices.getColumns().clear();
        int amountOfColumns = 1 + times_in_hours.length + 2;
        String[] columnHeaders = new String[amountOfColumns];
        columnHeaders[0] = VEHICLE_COLUMN_NAME;
        for (int i = 0; i < times_in_hours.length; i++) {
            columnHeaders[i + 1] = times_in_hours[i] + "";
        }

        columnHeaders[amountOfColumns - 1] = MAX_SLOTS_COLUMN_Name;
        columnHeaders[amountOfColumns - 2] = USABLE_SLOTS_COLUMN_Name;

        for (int i = 0; i < columnHeaders.length; i++) {

            TableColumn<RowData, String> column = new TableColumn<>(columnHeaders[i]);
            final int columnIndex = i; // For use in the PropertyValueFactory
            column.setCellValueFactory(data -> data.getValue().GetProperty(columnIndex));
            tabTablePrices.getColumns().add(column);
        }

        String[][] data = new String[priceLists.length][amountOfColumns];
        for (int i = 0; i < priceLists.length; i++) {
            data[i][0] = priceLists[i].vehicleType().displayName();
            for (int j = 0; j < times_in_hours.length; j++) {
                Float temp = priceLists[i].priceInEuro().get(times_in_hours[j]);
                if (temp == null) {
                    temp = CheckInCoreSystem.DEFAULT_COST;
                }
                data[i][j + 1] = temp + " " + CURRENCY_SIGN;
            }
            data[i][amountOfColumns - 2] = priceLists[i].spotUnused() + "";
            data[i][amountOfColumns - 1] = priceLists[i].spotAmount() + "";
        }

        ObservableList<RowData> rows = FXCollections.observableArrayList();
        for (String[] datum : data) {
            rows.add(new RowData(datum));
        }
        tabTablePrices.setItems(rows);

    }

    @FXML
    private TextField txtInfo;

    public void setTextSize(int size) {
        txtInfo.setStyle("-fx-font-size: " + size + "px;");
    }

    public void SetMessage(String message) {
        txtInfo.setText(message);
    }

    @FXML
    private void OnBtnDriveInPressed(ActionEvent ignoredEvent) {
        setChanged();
        notifyObservers(DriveInEvent.DriveIn);
    }

    @FXML
    private ChoiceBox<VehicleType> dpdVehicleChoice;
    public VehicleType GetSelectedVehicle() {
        return dpdVehicleChoice.getValue();
    }

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

    @FXML
    private ChoiceBox dpdStyle;

    public void PopulateStyleDropDown() {
        ObservableList<Uistyles> colors = FXCollections.observableArrayList(Uistyles.values());
        dpdStyle.setItems(colors);
    }

    @FXML
    private void OnBtnStylePressed(ActionEvent ignoredEvent) {
        setChanged();
        notifyObservers(DriveInEvent.StyleChanged);

    }

    public Uistyles GetSelectedStyle()  {
        return (Uistyles) dpdStyle.getSelectionModel().getSelectedItem();
    }
}
