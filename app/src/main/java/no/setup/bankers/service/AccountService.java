package no.setup.bankers.service;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import no.setup.bankers.domain.Account;
import no.setup.bankers.persistence.AccountRepo;
import no.setup.bankers.persistence.TransactionRepo;

public class AccountService implements IAccountService {
    public static final 
    String T_DEPOSIT="DEPOSIT", 
    T_WITHDRAW="WITHDRAW", 
    T_OUT="TRANSFER_OUT", 
    T_IN="TRANSFER_IN";

    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;

    public AccountService(
        AccountRepo accountRepo,
        TransactionRepo transactionRepo
    ) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    public int openAccount(
        Connection c,
        int owner_id,
        String iban,
        String createdAt
    ) {
        return accountRepo.create(
            c,
            owner_id,
            iban,
            createdAt
        );
    }

    @Override
    public Optional<Account> find(Connection c, int id) {
        return accountRepo.findById(c, id);
    }

    @Override
    public List<Account> listByOwner(Connection c, int ownerId) {
        return accountRepo.findByOwnerId(c, ownerId);
    }

    @Override
    public int balanceCents(Connection c, int id) {
        return transactionRepo.balanceCents(c, id);
    }

    @Override
    public TxResult deposit(
        Connection c,
        int accountId,
        int amountCents,
        String category,
        String message,
        String createdAt
    ) {
        if (amountCents <= 0) return TxResult.AMOUNT_MUST_BE_POSITIVE;

        if (accountRepo.findById(c, accountId).isEmpty()) return TxResult.ACCOUNT_NOT_FOUND;

        transactionRepo.create(
            c,
            accountId,
            amountCents,
            T_DEPOSIT,
            category,
            createdAt,
            message
        );

        return TxResult.OK;
    }

    @Override
    public TxResult withdraw(
        Connection c,
        int accountId,
        int amountCents,
        String category,
        String message,
        String createdAt
    ) {
        if (amountCents <= 0) return TxResult.AMOUNT_MUST_BE_POSITIVE;

        if (accountRepo.findById(c, accountId).isEmpty()) return TxResult.ACCOUNT_NOT_FOUND;

        int currentBalance = transactionRepo.balanceCents(c, accountId);
        if (amountCents > currentBalance) return TxResult.NOT_SUFFICIENT_FUNDS;


        transactionRepo.create(
            c, 
            accountId, 
            -amountCents, 
            T_WITHDRAW,
            category, 
            createdAt, 
            message
        );

        return TxResult.OK;
    }

    @Override
    public TxResult transfer(
        Connection c,
        int fromAccountId,
        int toAccountId,
        int amountCents,
        String category,
        String message,
        String createdAt
    ) {
        if (amountCents <= 0) return TxResult.AMOUNT_MUST_BE_POSITIVE;

        if (fromAccountId == toAccountId) return TxResult.INVALID_SAME_ACOUNT;

        boolean old = true;

        try {
            old = c.getAutoCommit();
            c.setAutoCommit(false);

            if (accountRepo.findById(c, toAccountId).isEmpty()) { 
                c.rollback(); 
                c.setAutoCommit(old); 
                return TxResult.ACCOUNT_NOT_FOUND; 
            } 
            if (accountRepo.findById(c, fromAccountId).isEmpty()) {
                c.rollback(); 
                c.setAutoCommit(old); 
                return TxResult.ACCOUNT_NOT_FOUND;
            }

            int currentBalance = transactionRepo.balanceCents(c, fromAccountId);

            if (amountCents > currentBalance) {
                c.rollback();
                c.setAutoCommit(old);
                return TxResult.NOT_SUFFICIENT_FUNDS;
            }

            transactionRepo.create(
                c, 
                fromAccountId, 
                -amountCents, 
                T_OUT, 
                category, 
                createdAt, 
                message
            );
            
            transactionRepo.create(
                c, 
                toAccountId, 
                amountCents, 
                T_IN, 
                category, 
                createdAt, 
                message
            );

            c.commit();
            c.setAutoCommit(old);

            return TxResult.OK;

        } catch (Exception e) {
            try { c.rollback(); } catch (Exception ignore) {}
            try { c.setAutoCommit(old);} catch (Exception ignore) {}
            throw new RuntimeException("transfer failed", e);
        }
    }
}
