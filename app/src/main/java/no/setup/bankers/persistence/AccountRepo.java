package no.setup.bankers.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import no.setup.bankers.domain.Account;

public class AccountRepo {
    private final Sql sql;
    public AccountRepo(Sql sql) {
        this.sql = sql;
    }

    public static Account mapAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("id"), 
                rs.getInt("owner_id"), 
                rs.getString("iban"), 
                rs.getString("created_at")
            );
    }

    public int create(
        Connection c, 
        int ownerId,
        String iban, 
        String createdAt
        ) {
        String q = "INSERT INTO accounts(owner_id, iban, created_at) VALUES (?, ?, ?)";
        return sql.insert(
            c,
            q,
            ps -> {
                ps.setInt(1, ownerId);
                ps.setString(2, iban);
                ps.setString(3, createdAt);
            }
        );
    }

    public Optional<Account> findById(Connection c, int id) {
        String q = "SELECT id, owner_id, iban, created_at FROM accounts WHERE id = ?";

        return sql.one(
            c, 
            q,
            ps -> ps.setInt(1, id),
            rs -> mapAccount(rs)
        );
    }

    public Optional<Account> findByIban(Connection c, String iban) {
        String q = "SELECT id, owner_id, iban, created_at FROM accounts WHERE iban = ?";

        return sql.one(
            c, 
            q, 
            ps -> ps.setString(1, iban),
            rs -> mapAccount(rs)
        );
        
    }
    
    public List<Account> findByOwnerId(Connection c, int ownerId) {
        String q = "SELECT id, owner_id, iban, created_at FROM accounts WHERE owner_id = ?";

        return sql.list(
            c, 
            q, 
            ps -> ps.setInt(1, ownerId),
            rs -> mapAccount(rs)
        );
    }
}
