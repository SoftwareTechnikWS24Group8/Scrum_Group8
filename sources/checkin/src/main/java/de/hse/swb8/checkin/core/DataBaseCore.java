package de.hse.swb8.checkin.core;

import de.hse.swb8.checkin.core.Enums.VehicleType;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DataBaseCore {

    private final DataBaseInfo info;
    private final Connection connection;


    public DataBaseCore(final @NotNull DataBaseInfo info) {
        Connection tryConnection = null;
        this.info = info;
        try {
            tryConnection = DriverManager.getConnection(info.url(), info.userName(), info.password());
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        connection = tryConnection;

        System.out.println("Connected to the database successfully.");
    }

    public VehicleType[] GetVehicleType()
    {
        List<VehicleType> vehicleTypes = new ArrayList<VehicleType>();
        try {
            try (Statement statement = connection.createStatement()) {

                String querry = "SELECT * FROM scrum.vehicle_types *";
                ResultSet resultSet = statement.executeQuery(querry);
                if (resultSet.next()) {
                    System.out.println(resultSet.getString("id"));
                    VehicleType temp = new VehicleType(resultSet.getInt("id"),resultSet.getString("vehicle_types"),resultSet.getString("display_name"));
                    vehicleTypes.add(temp);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        VehicleType[] vehicleTypesArray = new VehicleType[vehicleTypes.size()];
        vehicleTypesArray = vehicleTypes.toArray(vehicleTypesArray);
        return vehicleTypesArray;
    }
}
