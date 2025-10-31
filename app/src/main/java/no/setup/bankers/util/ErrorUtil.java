package no.setup.bankers.util;

import java.util.Map;

import org.sqlite.SQLiteException;

import io.javalin.http.Context;
import no.setup.bankers.persistence.Sql.DbException;

public class ErrorUtil {
    public static void handleSqlError(Context ctx, DbException e) {
        Throwable cause = e.getCause() != null ? e.getCause() : e;

        System.err.println("SQL error: " + cause.getMessage());

        if (cause instanceof SQLiteException se) {
            String msg = se.getMessage();
            if (msg != null && msg.contains("UNIQUE constraing failed")) {
                ctx.status(409).json(Map.of("error", msg));
                return;
            }
        }

        ctx.status(500).json(Map.of(
            "error", "Database error", 
            "details", cause.getMessage())
        );
    }
}
