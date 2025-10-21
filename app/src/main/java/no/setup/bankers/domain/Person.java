package no.setup.bankers.domain;

public record Person (
    int id,
    String firstname,
    String surname,
    String email,
    String phoneNumber
) {
}
