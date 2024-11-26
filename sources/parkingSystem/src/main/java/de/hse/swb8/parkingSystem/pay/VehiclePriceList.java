package de.hse.swb8.parkingSystem.pay;


import de.hse.swb8.parkingSystem.core.Records.VehicleType;

import java.util.Dictionary;

public record VehiclePriceList(VehicleType vehicleType, Dictionary<Float, Float> priceInEuro, int spotAmount,
                               int spotUnused) {
}
