package no.setup.bankers.domain;

public record Transaction (
    int id,
    int accountId, 
    int amountCents,
    String type,
    String category,
    String timestamp,
    String message
) {
    
}
