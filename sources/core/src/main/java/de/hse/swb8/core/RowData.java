package de.hse.swb8.pay.core;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RowData {
    private final StringProperty[] properties;

    public RowData(String[] rowValues) {
        properties = new StringProperty[rowValues.length];
        for (int i = 0; i < rowValues.length; i++) {
            properties[i] = new SimpleStringProperty(rowValues[i]);
        }
    }

    public StringProperty getProperty(int index) {
        if (index >= 0 && index < properties.length) {
            return properties[index];
        }
        throw new IllegalArgumentException("Invalid column index");
    }
}
