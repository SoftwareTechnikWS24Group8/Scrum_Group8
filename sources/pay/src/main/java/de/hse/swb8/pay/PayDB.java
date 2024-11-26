package de.hse.swb8.pay;

import de.hse.swb8.pay.core.DataBaseCore;
import de.hse.swb8.pay.core.Records.DataBaseInfo;
import de.hse.swb8.pay.core.Records.VehicleType;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Dictionary;
import java.util.Hashtable;

public class PayDB extends DataBaseCore {


    public PayDB(DataBaseInfo info) {
        super(info);
    }

    public String AddParkingVehicle(VehicleType selectedVehicle) {
        String Ticket = "";

        try {
            String query = "INSERT INTO scrum.park_list (vehicle_type_id, stamp_in_time, stamp_out_time, has_payed) " +
                    "VALUES (?, ?, NULL, ?)";
            String updateQuery = "UPDATE scrum.park_list SET ticket_name = ? WHERE id = ?";

            // Use a PreparedStatement for the SQL query
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                // Set parameters for the query
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                LocalDateTime localDateTime = currentTime.toLocalDateTime();

                int quersumme = calculateQuersumme(currentTime.getTime());
                preparedStatement.setInt(1, selectedVehicle.id()); // vehicle_type_id
                preparedStatement.setTimestamp(2, currentTime);    // stamp_in_time
                preparedStatement.setBoolean(3, false);            // has_payed

                // Execute the insert statement
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new RuntimeException("Inserting the park_list entry failed, no rows affected.");
                }

                int generatedId;
                // Retrieve the generated key (ID of the inserted row)
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);

                        // Create the Ticket string
                        Ticket = generatedId +"-"+selectedVehicle.id() +quersumme+localDateTime.getHour()+localDateTime.getDayOfWeek().getValue();
                        System.out.println(Ticket); // Output the result
                    } else {
                        throw new RuntimeException("Failed to retrieve the ID of the inserted park_list entry.");
                    }
                }

                // Update the ticket_name in the same row
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setString(1, Ticket);  // Set the ticket_name
                    updateStatement.setInt(2, generatedId); // Set the row ID
                    int updateRows = updateStatement.executeUpdate();
                    if (updateRows == 0) {
                        throw new RuntimeException("Updating the ticket_name failed, no rows affected.");
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Ticket;
    }

    private int calculateQuersumme(long number) {
        int sum = 0;
        while (number != 0) {
            sum += number % 10; // Add the last digit
            number /= 10;       // Remove the last digit
        }
        return sum;
    }


    public Dictionary<Float,Float> GetPriceList(VehicleType selectedVehicle)
    {
        Dictionary<Float,Float> list = new Hashtable<Float,Float>();
        try {
            try (Statement statement = connection.createStatement()) {

                String querry = "SELECT t.time_hours, v.price " +
                        "FROM scrum.hours_list t " +
                        "JOIN scrum.price_list v ON t.id = v.duration_id " +
                        "WHERE v.vehicle_type_id =" + selectedVehicle.id();
                ResultSet resultSet = statement.executeQuery(querry);
                while (resultSet.next()) {
                    list.put(resultSet.getFloat("time_hours"),resultSet.getFloat("price"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(list.isEmpty())
        {
            System.out.println("No rows found");
            System.exit(1);
        }
        // 3 hours > 2.3€
        return list;
    }

    public Dictionary<Integer,Float> GetPriceHeaders()
    {
        Dictionary<Integer,Float> list = new Hashtable<Integer,Float>();
        try {
            try (Statement statement = connection.createStatement()) {

                String querry = "SELECT * FROM scrum.hours_list";
                ResultSet resultSet = statement.executeQuery(querry);
                while (resultSet.next()) {
                    list.put(resultSet.getInt("id"),resultSet.getFloat("time_hours"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(list.isEmpty())
        {
            System.out.println("No rows found");
            System.exit(1);
        }
        return list;
    }

    public int CheckMaxAmountSpots(VehicleType vehicleType) {

        int maxAmount = 0;
        try {
            try (Statement statement = connection.createStatement()) {

                String querry = "SELECT SUM(spot_amount) AS total_spots FROM scrum.park_spots WHERE vehicle_type_id =" + vehicleType.id();
                ResultSet resultSet = statement.executeQuery(querry);
                if (resultSet.next()) {
                    maxAmount = resultSet.getInt("total_spots");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return maxAmount;
    }


    public int CheckSpotUsed(VehicleType type) {
        int maxAmount = 0;
        try {
            try (Statement statement = connection.createStatement()) {

                String querry = "SELECT COUNT(*) AS used_spots FROM scrum.park_list WHERE vehicle_type_id ="+ type.id() +" AND stamp_out_time IS NULL";
                ResultSet resultSet = statement.executeQuery(querry);
                if (resultSet.next()) {
                    maxAmount = resultSet.getInt("used_spots");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return maxAmount;
    }

    public int CheckSpotsNotUsed(VehicleType type) {
        int maxAmount = CheckMaxAmountSpots(type);
        int usedSpots = CheckSpotUsed(type);

        return maxAmount-usedSpots;
    }
}
