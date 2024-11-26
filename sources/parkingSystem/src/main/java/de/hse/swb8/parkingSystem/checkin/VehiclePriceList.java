package de.hse.swb8.checkin.checkin;

import de.hse.swb8.checkin.core.Records.VehicleType;

import java.util.Dictionary;

public record VehiclePriceList(VehicleType vehicleType, Dictionary<Float,Float> priceInEuro, int spotAmount, int spotUnused) {
}
