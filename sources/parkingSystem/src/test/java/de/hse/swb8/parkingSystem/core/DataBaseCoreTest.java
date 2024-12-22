package de.hse.swb8.parkingSystem.core;

import de.hse.swb8.parkingSystem.core.Records.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataBaseCoreTest {

    private Connection mockConnection;
    private Statement mockStatement;
    private ResultSet mockResultSet;
    private DataBaseCore dataBaseCore;

    @BeforeEach
    void setUp() throws Exception {
        // Mock the Connection, Statement, and ResultSet
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);

        // Stub the connection to return the mocked statement
        when(mockConnection.createStatement()).thenReturn(mockStatement);


        // Inject the mocked connection into the DataBaseCore
        dataBaseCore = new DataBaseCore(mockConnection);
    }

    @Test
    void testGetVehicleTypes() throws Exception {

        // Stub the statement to return the mocked result set
        when(mockStatement.executeQuery("SELECT * FROM scrum.vehicle_types")).thenReturn(mockResultSet);

        // Stub the result set to return mock data
        when(mockResultSet.next()).thenReturn(true, true, false); // Two rows of data
        when(mockResultSet.getInt("vehicle_type_id")).thenReturn(1, 2);
        when(mockResultSet.getString("display_name")).thenReturn("Car", "Bike");

        VehicleType[] vehicleTypes = dataBaseCore.GetVehicleTypes();

        // Verify the results
        assertNotNull(vehicleTypes);
        assertEquals(2, vehicleTypes.length);

        assertEquals(1, vehicleTypes[0].id());
        assertEquals("Car", vehicleTypes[0].displayName());

        assertEquals(2, vehicleTypes[1].id());
        assertEquals("Bike", vehicleTypes[1].displayName());

        // Verify interactions with the mock
        verify(mockConnection).createStatement();
        verify(mockStatement).executeQuery("SELECT * FROM scrum.vehicle_types");
        verify(mockResultSet, times(3)).next();
    }
}
