package de.hse.swb8.pay;

import de.hse.swb8.pay.core.DataBaseCore;
import de.hse.swb8.pay.core.Records.DataBaseInfo;
import de.hse.swb8.pay.core.Records.VehicleType;

import java.sql.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class PayDB extends DataBaseCore {


    public PayDB(DataBaseInfo info) {
        super(info);
    }

    public boolean DoesTicketExistValid(String Ticket_id)
    {
        boolean isActive = false;
        String query = "SELECT COUNT(*) FROM scrum.park_list WHERE ticket_name = ? AND stamp_out_time IS NULL";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        // Set the ticket_name parameter
        preparedStatement.setString(1, Ticket_id);

        // Execute the query
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                // Get the count from the result
                int count = resultSet.getInt(1);
                isActive = count > 0; // If count > 0, the ticket is active
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Error checking active ticket", e);
    }
    return isActive;
    }

    public VehicleType GetVehicleTypeFromTicketID(String Ticket_id)
    {
        VehicleType type = null;
        String query = "SELECT * FROM scrum.park_list WHERE ticket_name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the ticket_name parameter
            preparedStatement.setString(1, Ticket_id);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Get the count from the result
                    int count = resultSet.getInt("vehicle_type_id");
                    type = new VehicleType(count, "");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking active ticket", e);
        }
        return type;
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

    public float getHoursBetweenStampFromTicketAndNow(String ticket) {
        String query = "SELECT stamp_in_time, stamp_out_time FROM scrum.park_list WHERE ticket_name = ?";
        Timestamp stampIn = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the ticket_name parameter
            preparedStatement.setString(1, ticket);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve the timestamps
                    stampIn = resultSet.getTimestamp("stamp_in_time");

                } else {
                    throw new RuntimeException("No record found for the provided ticket: " + ticket);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving timestamps from database", e);
        }

        // Calculate hours between timestamps
        return calculateHoursBetweenTimestamps(stampIn, new Timestamp(System.currentTimeMillis()));
    }

    // Helper method to calculate hours between two timestamps
    private float calculateHoursBetweenTimestamps(Timestamp stampIn, Timestamp stampOut) {
        long millisecondsDifference = stampOut.getTime() - stampIn.getTime();
        return millisecondsDifference / (1000f * 60 * 60); // Convert milliseconds to hours as a float
    }

    public void SetPayed(String ticket_id , float payed_amount)
    {
        String query = "UPDATE scrum.park_list SET has_payed = ?, pay_time = ?, payed_amount = ? WHERE ticket_name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the parameters
            preparedStatement.setBoolean(1, true); // Set has_payed to true
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // Set payed_time to current time
            preparedStatement.setFloat(3, payed_amount); // Set payed_time to current time
            preparedStatement.setString(4, ticket_id); // Specify the ticket_name

            // Execute the update
            int affectedRows = preparedStatement.executeUpdate();

            // Check if the update was successful
            if (affectedRows == 0) {
                throw new RuntimeException("No rows were updated. Ticket not found: " + ticket_id);
            } else {
                System.out.println("Payment updated successfully for ticket: " + ticket_id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating payment status for ticket: " + ticket_id, e);
        }
    }

    /*
    //TODO move into checkout
    public void addRowToPayedList(String ticket, float payedInEuro) {
        String selectQuery = "SELECT id, vehicle_type_id, stamp_in_time, stamp_out_time FROM scrum.park_list WHERE ticket_name = ?";
        String insertQuery = "INSERT INTO scrum.payed_list (vehicle_type_id, ticket_id, payed_in_euro, parked_time) VALUES (?, ?, ?, ?)";

        try {
            // Step 1: Retrieve data from park_list
            int vehicleTypeId;
            Timestamp stampIn;
            Timestamp stampOut;
            int ticketId;

            try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
                selectStmt.setString(1, ticket);
                try (ResultSet resultSet = selectStmt.executeQuery()) {
                    if (resultSet.next()) {
                        ticketId = resultSet.getInt("id");
                        vehicleTypeId = resultSet.getInt("vehicle_type_id");
                        stampIn = resultSet.getTimestamp("stamp_in_time");
                        stampOut = resultSet.getTimestamp("stamp_out_time");

                        if (stampOut == null) {
                            throw new IllegalStateException("Stamp out time is null for ticket: " + ticket);
                        }
                    } else {
                        throw new RuntimeException("No record found in park_list for ticket: " + ticket);
                    }
                }
            }

            // Step 2: Calculate parked time in hours
            float parkedTime = calculateHoursBetweenTimestamps(stampIn, stampOut);

            // Step 3: Insert a new row into payed_list
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, vehicleTypeId); // vehicle_type_id
                insertStmt.setString(2, ticket);    // ticket_id
                insertStmt.setFloat(3, payedInEuro); // payed_in_euro
                insertStmt.setFloat(4, parkedTime);  // parked_time

                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted == 0) {
                    throw new RuntimeException("Failed to insert row into payed_list for ticket: " + ticket);
                } else {
                    System.out.println("Successfully added payment record for ticket: " + ticket);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding row to payed_list for ticket: " + ticket, e);
        }
    }*/
}
