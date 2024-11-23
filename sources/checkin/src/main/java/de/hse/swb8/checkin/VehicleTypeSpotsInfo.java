package de.hse.swb8.checkin;


import de.hse.swb8.checkin.core.Enums.VehicleType;

public record VehicleTypeSpotsInfo(VehicleType vehicleType, String vehicleName, int SpotAmounts, int usedSpots){
}
