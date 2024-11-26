package de.hse.swb8.pay;

import de.hse.swb8.pay.core.Records.VehicleType;

import java.util.Dictionary;

public record VehiclePriceList(VehicleType vehicleType, Dictionary<Float,Float> priceInEuro, int spotAmount, int spotUnused) {
}
