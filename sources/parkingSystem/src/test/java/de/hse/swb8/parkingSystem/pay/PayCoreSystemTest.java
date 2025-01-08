package de.hse.swb8.parkingSystem.pay;

import de.hse.swb8.parkingSystem.core.DataBaseCore;
import de.hse.swb8.parkingSystem.core.Records.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PayCoreSystemTest {

    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private PayDB payDB;

    private PayCoreSystem payCore;

    @BeforeEach
    void setUp() throws Exception {
        // Mock the Connection, Statement, and ResultSet
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        // Stub the connection to return the mocked statement
        when(mockConnection.createStatement()).thenReturn(mockStatement);


        // Inject the mocked connection into the PayDB
        payDB = new PayDB(mockConnection);
        payCore = new PayCoreSystem(payDB);
    }

    @Test
    void getDuration() throws Exception {
        String mockTicket = "123456";
        String query = "SELECT stamp_in_time, stamp_out_time FROM scrum.park_list WHERE ticket_name = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getTimestamp("stamp_in_time")).thenReturn(new Timestamp(
                System.currentTimeMillis() - (1000 * 60 * 60 * 2) - (1000 * 60 * 30)));


        String duration2h = payCore.getDuration(mockTicket);

        assertEquals("02:30h", duration2h);
    }
}