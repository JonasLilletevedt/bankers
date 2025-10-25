package no.setup.bankers.service;

public enum TxResult {
    INSUFFICIENT_FUNDS,
    ACCOUNT_NOT_FOUND,
    INVALID_SAME_ACCOUNT,
    AMOUNT_MUST_BE_POSITIVE,
    OK
}
