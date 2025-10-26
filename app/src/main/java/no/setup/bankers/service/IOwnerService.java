package no.setup.bankers.service;

import java.sql.Connection;

import no.setup.bankers.domain.Owner;

public interface IOwnerService {

    public int createOwner(
        Connection c,
        String firstname,
        String surname,
        String email,
        String phonenumber
    );

    public int getOwnerIdFromEmail(Connection c, String email);

}
