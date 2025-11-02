package no.setup.bankers.service;

import java.sql.Connection;
import java.util.Optional;

import no.setup.bankers.domain.Owner;

public interface IOwnerService {

    int createOwner(
        Connection c,
        String firstname,
        String surname,
        String email,
        String phonenumber
    );

    int getOwnerIdFromEmail(Connection c, String email);

    Optional<Owner> getOwnerFromOwnerId(Connection c, int ownerId);

}
