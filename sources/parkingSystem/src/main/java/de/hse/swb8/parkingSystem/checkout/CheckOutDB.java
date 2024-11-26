package de.hse.swb8.parkingSystem.checkout;

import de.hse.swb8.parkingSystem.core.DataBaseCore;
import de.hse.swb8.parkingSystem.core.Records.DataBaseInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CheckOutDB extends DataBaseCore {


    public CheckOutDB(DataBaseInfo info) {
        super(info);
    }

    public boolean DoesTicketExistValid(String Ticket_id) {
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

    public float getHoursBetweenStampFromTicketAndNow(String ticket) {
        String query = "SELECT pay_time FROM scrum.park_list WHERE ticket_name = ?";
        Timestamp stampIn;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the ticket_name parameter
            preparedStatement.setString(1, ticket);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve the timestamps
                    stampIn = resultSet.getTimestamp("pay_time");

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


    public float GetAlreadyPayedMoney(String ticketId) {

        String query = "SELECT payed_amount FROM scrum.park_list WHERE ticket_name = ?";
        float payed_amount;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the ticket_name parameter
            preparedStatement.setString(1, ticketId);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    payed_amount = resultSet.getFloat("payed_amount");

                } else {
                    throw new RuntimeException("No record found for the provided ticket: " + ticketId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving timestamps from database", e);
        }

        return payed_amount;
    }

    public void SetCheckoutTime(String tickedId) {

        String query = "UPDATE scrum.park_list SET stamp_out_time = ? WHERE ticket_name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the parameters
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis())); // Set payed_time to current time
            preparedStatement.setString(2, tickedId); // Specify the ticket_name

            // Execute the update
            int affectedRows = preparedStatement.executeUpdate();

            // Check if the update was successful
            if (affectedRows == 0) {
                throw new RuntimeException("No rows were updated. Ticket not found: " + tickedId);
            } else {
                System.out.println("Checkout time updated successfully for ticket: " + tickedId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Checkout time for ticket: " + tickedId, e);
        }
    }


    public void AddRowToPayedList(String ticket, float payedInEuro) {
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
    }

    public boolean HasTicketBeenPayed(String ticketId) {
        String query = "SELECT pay_time FROM scrum.park_list WHERE ticket_name = ?";
        boolean payed;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the ticket_name parameter
            preparedStatement.setString(1, ticketId);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    payed = resultSet.getTimestamp("pay_time") != null;

                } else {
                    throw new RuntimeException("No record found for the provided ticket: " + ticketId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving timestamps from database", e);
        }

        return payed;
    }
}
