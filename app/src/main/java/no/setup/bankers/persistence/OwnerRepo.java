package no.setup.bankers.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import no.setup.bankers.domain.Owner;

public class OwnerRepo {
    private final Sql sql;
    public OwnerRepo(Sql sql) {
        this.sql = sql;
    }

    public static Owner mapOwner(ResultSet rs) throws SQLException {
        return new Owner(
            rs.getInt("id"),
            rs.getString("firstname"),
            rs.getString("surname"),
            rs.getString("email"),
            rs.getString("phonenumber")
        );
    }

    public int create(
        Connection c, 
        String firstname, 
        String surname,
        String email,
        String phonenumber
    ) {
        String q = "INSERT INTO owners(firstname, surname, email, phonenumber) VALUES (?, ?, ?, ?)";
        return sql.insert(
            c,
            q,
            ps -> {
                ps.setString(1, firstname);
                ps.setString(2, surname);
                ps.setString(3, email);
                ps.setString(4, phonenumber);
            }
        );
    }

    public Optional<Owner> findById(Connection c, int id) {
        String q = "SELECT id, firstname, surname, email, phonenumber FROM owners WHERE id = ?";
        
        return sql.one(
            c, 
            q, 
            ps -> ps.setInt(1, id),
            rs -> mapOwner(rs)
        );
    }

}
