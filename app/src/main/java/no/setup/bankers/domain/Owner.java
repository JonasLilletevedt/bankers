package no.setup.bankers.domain;

public record Owner (
    int id,
    String firstname,
    String surname,
    String email,
    String phoneNumber
) {
}
