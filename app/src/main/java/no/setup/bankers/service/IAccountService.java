package no.setup.bankers.service;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import no.setup.bankers.domain.Account;

public interface IAccountService {
    int openAccount(Connection c, int owner_id, String iban, String createdAt);

    Optional<Account> find(Connection c, int id);

    List<Account> listByOwner(Connection c, int ownerId);

    int balanceCents(Connection c, int id);

    public TxResult deposit(
        Connection c,
        int accountId, 
        int amountCents, 
        String category, 
        String message, 
        String createdAt
    );

    public TxResult withdraw(
        Connection c,
        int accountId,
        int amountCents, 
        String category, 
        String message,
        String createdAt
    );

    public TxResult transfer(
        Connection c,
        int fromAccountId,
        int toAccountId,
        int amountCents,
        String category,
        String message,
        String createdAt
    );
}

