package de.hse.swb8.parkingSystem.core;

import de.hse.swb8.parkingSystem.core.Records.DataBaseInfo;
import de.hse.swb8.parkingSystem.core.Records.VehicleType;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DataBaseCore {

    protected final Connection connection;

    public DataBaseCore(final @NotNull DataBaseInfo dbInfo) {
        Connection tryConnection = null;
        if (!DataBaseCore.ValidateDataBaseInfo(dbInfo)) {
            throw new RuntimeException("Tried creating DataBase without valid Database");
        }
        try {
            tryConnection = DriverManager.getConnection(dbInfo.url(), dbInfo.userName(), dbInfo.password());
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        connection = tryConnection;
    }

    public VehicleType[] GetVehicleTypes() {
        List<VehicleType> vehicleTypes = new ArrayList<>();
        try {
            try (Statement statement = connection.createStatement()) {

                String querry = "SELECT * FROM scrum.vehicle_types";

                ResultSet resultSet = statement.executeQuery(querry);

                while (resultSet.next()) {
                    int vehicle_type_id = resultSet.getInt("vehicle_type_id");
                    String vehicle_display_name = resultSet.getString("name");
                    VehicleType temp = new VehicleType(vehicle_type_id, vehicle_display_name);
                    vehicleTypes.add(temp);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        VehicleType[] vehicleTypesArray = new VehicleType[vehicleTypes.size()];
        return vehicleTypes.toArray(vehicleTypesArray);
    }

    public static boolean ValidateDataBaseInfo(final @NotNull DataBaseInfo info) {
        // Load the database driver if required (for older JDBC versions).
        // For newer JDBC versions, the driver is auto-registered.
        try (Connection ignored = DriverManager.getConnection(info.url(), info.userName(), info.password())) {
            // If we reach here, the connection is successful
            System.out.println("Connection successful!");
            return true;
        } catch (SQLException e) {
            // Handle specific SQL exceptions
            if (e.getSQLState().equals("28000")) { // Invalid authorization
                System.out.println("Invalid credentials: " + e.getMessage());
            } else {
                System.out.println("Database connection error: " + e.getMessage());
            }
            return false;
        }
    }
}
