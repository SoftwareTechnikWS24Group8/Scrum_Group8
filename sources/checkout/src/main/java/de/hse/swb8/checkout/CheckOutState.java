package de.hse.swb8.checkout;

public record CheckOutState(String ticket_id, float priceInEuro, boolean payed) {
}
