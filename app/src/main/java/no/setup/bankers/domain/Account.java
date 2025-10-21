package no.setup.bankers.domain;

public record Account(
    int id,
    int ownerId,
    String iban,
    String createdAt
) { }
