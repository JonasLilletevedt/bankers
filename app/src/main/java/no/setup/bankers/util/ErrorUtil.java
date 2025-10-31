package no.setup.bankers.util;

import java.util.Map;

import org.sqlite.SQLiteException;

import io.javalin.http.Context;

public class ErrorUtil {
    public static void handleUniqueConstraint(Context ctx, Exception e) {
        Throwable cause = e.getCause() != null ? e.getCause() : e;

        System.err.println("SQL error: " + cause.getMessage());

        if (cause instanceof SQLiteException se) {
            String msg = se.getMessage();
            if (msg != null && msg.contains("UNIQUE constraing failed")) {
                ctx.status(409).json(Map.of("error", msg));
                return;
            }
        }

        ctx.status(500).json(Map.of("error", "Database error: " + cause.getMessage()));
    }
}
