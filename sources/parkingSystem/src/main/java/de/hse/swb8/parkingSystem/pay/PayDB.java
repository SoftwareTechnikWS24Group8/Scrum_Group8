package de.hse.swb8.parkingSystem.pay;


import de.hse.swb8.parkingSystem.core.DataBaseCore;
import de.hse.swb8.parkingSystem.core.Records.DataBaseInfo;
import de.hse.swb8.parkingSystem.core.Records.VehicleType;

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
        Dictionary<Float,Float> list = new Hashtable<>();
        try {
            try (Statement statement = connection.createStatement()) {

                String query = "SELECT t.time_hours, v.price " +
                        "FROM scrum.hours_list t " +
                        "JOIN scrum.price_list v ON t.id = v.duration_id " +
                        "WHERE v.vehicle_type_id =" + selectedVehicle.id();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    float price = resultSet.getFloat("price");
                    list.put(resultSet.getFloat("time_hours"),price);
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
        Timestamp stampIn;

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
        String query = "SELECT payed_amount FROM scrum.park_list WHERE ticket_name = ?";

        float alreadyPayed = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the ticket_name parameter
            preparedStatement.setString(1, ticket_id);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    alreadyPayed = resultSet.getFloat("payed_amount");

                } else {
                    throw new RuntimeException("No record found for the provided ticket: " + ticket_id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving timestamps from database", e);
        }


        query = "UPDATE scrum.park_list SET has_payed = ?, pay_time = ?, payed_amount = ? WHERE ticket_name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the parameters
            preparedStatement.setBoolean(1, true); // Set has_payed to true
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // Set payed_time to current time
            preparedStatement.setFloat(3, payed_amount+alreadyPayed); // Set payed_time to current time
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

}
