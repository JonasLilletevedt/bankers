package no.setup.bankers.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import no.setup.bankers.domain.Transaction;

public class TransactionRepo {
    private final Sql sql;
    public TransactionRepo(Sql sql) {
        this.sql = sql;
    }

    public static Transaction mapTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
            rs.getInt("id"),
            rs.getInt("account_id"),
            rs.getInt("amount_cents"),
            rs.getString("type"),
            rs.getString("category"),
            rs.getString("created_at"),
            rs.getString("message")
        );
    }

    public int create(
        Connection c,
        int accountId,
        int amountCents,
        String type,
        String category,
        String createdAt,
        String message
    ) {
        String q = "INSERT INTO transactions(account_id, amount_cents, type, category, timestamp, message) VALUES (?, ?, ?, ?, ?, ?)";

        return sql.insert(c, 
            q, 
            ps -> {
                ps.setInt(1, accountId);
                ps.setInt(2, amountCents);
                ps.setString(3, type);
                ps.setString(4, category);
                ps.setString(5, createdAt);
                ps.setString(6, message);
            }
        );
    }

    public Optional<Transaction> findById(Connection c, int id) {
        String q = "SELECT id, account_id, amount_cents, type, category, created_at, message FROM transactions WHERE id = ?";

        return sql.one(
            c, 
            q,
            ps -> ps.setInt(1, id),
            rs -> mapTransaction(rs)
        );
    }
}
