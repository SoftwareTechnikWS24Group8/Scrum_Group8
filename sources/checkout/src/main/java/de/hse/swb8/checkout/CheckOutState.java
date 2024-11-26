package de.hse.swb8.pay;

public record PayState(String ticket_id, float priceInEuro, boolean payed) {
}
