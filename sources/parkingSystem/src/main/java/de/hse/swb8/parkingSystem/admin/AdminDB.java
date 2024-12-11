package de.hse.swb8.parkingSystem.admin;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import de.hse.swb8.parkingSystem.core.DataBaseCore;
import de.hse.swb8.parkingSystem.core.Records.DataBaseInfo;

public class AdminDB extends DataBaseCore {

    public AdminDB(DataBaseInfo info) {
        super(info);
    }
    public double GetRevenueSinceDate(LocalDate date) {
        double totalRevenue = 0.0;
        String query = "SELECT SUM(payed_amount) FROM scrum.payed_list WHERE payed_list.parked_start_date >= ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the date parameter
            preparedStatement.setDate(1, Date.valueOf(date));

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
