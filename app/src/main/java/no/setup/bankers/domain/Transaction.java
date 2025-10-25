package no.setup.bankers.domain;

public record Transaction (
    int id,
    int accountId, 
    int amountCents,
    String type,
    String category,
    String createdAt,
    String message
) {
    
}
