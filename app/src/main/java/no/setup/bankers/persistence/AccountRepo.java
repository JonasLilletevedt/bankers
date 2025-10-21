package no.setup.bankers.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import no.setup.bankers.domain.Account;

public class AccountRepo {
    public int create(
        Connection c, 
        int ownerId,
        String iban, 
        String createdAt
        ) {
        String sql = "INSERT INTO accounts(owner_id, iban, created_at) VALUES (?, ?, ?)";
        
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
        String sql = "SELECT id, owner_id, iban, created_at FROM accounts WHERE id = ?";

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

    public Optional<Account> findByIban(Connection c, String iban) {
        String sql = "SELECT id, owner_id, iban, created_at FROM accounts WHERE iban = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, iban);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not find account: " + iban, e);
        }
    }
    
    public List<Account> findByOwnerId(Connection c, int ownerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT id, owner_id, iban, created_at FROM accounts WHERE owner_id = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()){
                    Account account = new Account(
                        rs.getInt("id"), 
                        rs.getInt("owner_id"), 
                        rs.getString("iban"), 
                        rs.getString("created_at")
                    );
                    accounts.add(account);
                }
            }
            return accounts;
        } catch (SQLException e) {
            throw new RuntimeException("Could not find accounts for owner: " + ownerId, e);
        }
    }
}
