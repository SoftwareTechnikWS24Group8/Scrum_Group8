package de.hse.swb8.parkingSystem.admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import de.hse.swb8.parkingSystem.core.DataBaseCore;
import de.hse.swb8.parkingSystem.core.Records.DataBaseInfo;

public class AdminDB extends DataBaseCore {

    public AdminDB(DataBaseInfo info) {
        super(info);
    }
    public double GetRevenueSinceDate(Date date) {
        double totalRevenue = 0.0;
        String query = "SELECT SUM(amount) FROM scrum.park_list WHERE ticket_date >= ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the date parameter
            preparedStatement.setDate(1, new java.sql.Date(date.getTime()));

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve the total revenue (could be null if no results)
                    totalRevenue = resultSet.getDouble(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating revenue", e);
        }
        return totalRevenue;
    }


}
