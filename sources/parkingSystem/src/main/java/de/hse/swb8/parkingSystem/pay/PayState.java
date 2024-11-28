package de.hse.swb8.parkingSystem.pay;

public record PayState(String ticket_id, float priceInEuro, boolean payed) {
}
