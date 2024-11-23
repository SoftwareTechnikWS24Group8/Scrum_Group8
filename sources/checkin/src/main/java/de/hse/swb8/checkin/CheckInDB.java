package de.hse.swb8.checkin;

import de.hse.swb8.checkin.core.DataBaseCore;
import de.hse.swb8.checkin.core.DataBaseInfo;
import de.hse.swb8.checkin.core.Enums.VehicleType;

import java.util.ArrayList;
import java.util.List;

public class CheckInDB extends DataBaseCore {


    public CheckInDB(DataBaseInfo info) {
        super(info);
    }

    public int CheckSpotAvailable(VehicleType type) {
        //TODO implementen
        return 0;
    }

    public String AddParkingVehicle(VehicleType selectedVehicle) {
        //TODO
        /*
        Add to parking vehicle new row with current DateTime as start

        and return generated ID Ticket
         */

        return "TICKETXXX";
    }

    public String[][] GetPriceList()
    {
        //TODO
        String[][] list = new String[10][10];

        return list;
    }

    public String[] GetPriceHeaders()
    {
        //TODO
        String[] headers = new String[10];
        return headers;
    }

    public VehicleTypeSpotsInfo[] GetParkInfos() {
        //TODO get all vehcile type, name of vehicle type , spots avaiable and spots max

        VehicleTypeSpotsInfo[] infos = new VehicleTypeSpotsInfo[10];
        return infos;
    }
}
