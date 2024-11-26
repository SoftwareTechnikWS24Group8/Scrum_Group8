package de.hse.swb8.checkin;

import de.hse.swb8.checkin.core.Enums.VehicleType;

import java.util.Dictionary;

public record VehiclePriceList(VehicleType vehicleType, Dictionary<Float,Float> priceInEuro, int spotAmount, int spotUnused) {
}
