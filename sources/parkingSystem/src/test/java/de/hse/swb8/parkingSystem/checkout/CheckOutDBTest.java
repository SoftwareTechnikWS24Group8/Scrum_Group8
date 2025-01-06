
package de.hse.swb8.parkingSystem.checkout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckOutDBTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private CheckOutDB checkOutDB;

    @BeforeEach
    void setUp() throws Exception {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);


        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Inject the mocked connection into the CheckOutDB
        checkOutDB = new CheckOutDB(mockConnection);
    }

    @Test
    void doesTicketExistValid() throws Exception {
        String query = "SELECT COUNT(*) FROM scrum.park_list WHERE ticket_name = ? AND stamp_out_time IS NULL";
        String testTicket = "ABC123";

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true); 
        when(mockResultSet.getInt(1)).thenReturn(1); 

        boolean result = checkOutDB.DoesTicketExistValid(testTicket);

        assertTrue(result);

        verify(mockConnection).prepareStatement(query);
        verify(mockPreparedStatement).setString(1, testTicket);
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet).next();
        verify(mockResultSet).getInt(1);
    }
}