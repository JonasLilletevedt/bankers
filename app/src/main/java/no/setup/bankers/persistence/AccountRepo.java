package no.setup.bankers.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import no.setup.bankers.domain.Account;

public class AccountRepo {
    public int create(
        Connection c, 
        int ownerId,
        String iban, 
        String createdAt
        ) {
        String sql = "Insert INTO accounts(owner_id, iban, created_at) VALUES (?, ?, ?)";
        
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ownerId);
            ps.setString(2, iban);
            ps.setString(3, createdAt);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("No ID returned");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Insertion of new account failed", e);
        }
    }

    public Optional<Account> findById(Connection c, int accountId) {
        String sql = "SELECT id, owner_id, createdAt FROM accounts where id = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                Account account = new Account(
                    rs.getInt("id"), 
                    rs.getInt("owner_id"), 
                    rs.getString("iban"), 
                    rs.getString("created_at")
                );
                return Optional.of(account);
            } else {
                return Optional.empty();
            }
                        
        } catch (SQLException e) {
            throw new RuntimeException("Could not find account: " + accountId, e);
        }
    }
}
