package de.hse.swb8.parkingSystem.admin;

import de.hse.swb8.parkingSystem.core.DataBaseCore;
import de.hse.swb8.parkingSystem.core.Records.DataBaseInfo;
import de.hse.swb8.parkingSystem.core.Records.VehicleType;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Dictionary;
import java.util.Hashtable;

public class AdminDB extends DataBaseCore {


    public AdminDB(DataBaseInfo info) {
        super(info);
    }

}
