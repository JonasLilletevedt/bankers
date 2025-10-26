package no.setup.bankers.persistence;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class Db {
    private final String path;

    public Db(String path) { this.path = path; }

    public Connection connect() throws SQLException {
        String dbPath = System.getenv().getOrDefault("DB_PATH", path);
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

        try (Statement s = c.createStatement()) {
            s.execute("PRAGMA foreign_keys = ON");
        } catch (Exception e) {
            throw new RuntimeException("Connection to DB failed", e);
        }
        return c;
    }

    public String readResource(String path) {
        try (var data = Db.class.getResourceAsStream(path)) {
            if (data == null) {
                throw new IllegalStateException("Resource not found: " + path);
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(data))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resource: " + path, e);
        }
    }

    // TODO: replace naive SQL splitter if we add complex migrations
    // TODO: add busy timeout 
    public void migrate() {
        try (Connection c = connect(); Statement st = c.createStatement()) {
            boolean conOldSetting = c.getAutoCommit();
            c.setAutoCommit(false);
            String sqlSchema = readResource("/db/migrations/001_schema.sql");
            String[] schemaStatements = sqlSchema.split(";");
            try {
                for (String schema_st : schemaStatements) {
                    String s = schema_st.trim();
                    if (s.isEmpty() || s.startsWith("--"))
                        continue;
                    st.execute(s);
                }
                c.commit();
            } catch (Exception e) {
                c.rollback();
                throw new RuntimeException("Migration Failed", e);
            } finally {
                c.setAutoCommit(conOldSetting);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not migrate database", e);
        }
    }

}
