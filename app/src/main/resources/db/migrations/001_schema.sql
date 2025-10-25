PRAGMA foreign_keys
= ON;

CREATE TABLE
IF NOT EXISTS owners
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    firstname TEXT NOT NULL,
    surname TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    phone_number TEXT NOT NULL UNIQUE
);

CREATE TABLE
IF NOT EXISTS accounts
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    owner_id INTEGER NOT NULL,
    iban TEXT NOT NULL UNIQUE,
    created_at TEXT NOT NULL,
    FOREIGN KEY
(owner_id) REFERENCES persons
(id) ON
DELETE RESTRICT
);

CREATE TABLE
IF NOT EXISTS transactions
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    account_id INTEGER NOT NULL,
    amount_cents INTEGER NOT NULL,
    type TEXT NOT NULL CHECK
(type IN
('DEPOSIT', 'WITHDRAW', 'TRANSFER_IN', 'TRANSFER_OUT')),
    category TEXT NOT NULL,
    created_at TEXT NOT NULL,
    message TEXT,
    FOREIGN KEY
(account_id) REFERENCES accounts
(id) ON
DELETE CASCADE
);

CREATE INDEX
IF NOT EXISTS idx_transactions_account_id ON transactions
(account_id);