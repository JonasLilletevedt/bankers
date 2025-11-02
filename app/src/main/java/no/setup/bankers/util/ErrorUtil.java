package no.setup.bankers.util;
import java.sql.SQLException;

import io.javalin.http.Context;
import no.setup.bankers.http.ApiResponses;
import no.setup.bankers.persistence.Sql;

public final class ErrorUtil {

    private ErrorUtil() {}

    public static void handleSqlError(Context ctx, Sql.DbException e) {
        Throwable cause = e.getCause();

        if (cause instanceof SQLException sqlEx) {
            String state = sqlEx.getSQLState();
            // You can branch on state if you want:
            if (state != null && state.startsWith("23")) {
                // constraint violation, unique, FK, etc.
                ApiResponses.badRequest(ctx, "Database constraint violated");
                return;
            }
            // fallthrough -> generic DB error
            ApiResponses.serverError(ctx, "Database error: " + sqlEx.getMessage());
            return;
        }

        ApiResponses.serverError(ctx, "Database error: " + e.getMessage());
    }

    public static void handleUnexpected(Context ctx, Exception e) {
        ApiResponses.serverError(ctx, "Unexpected error: " + e.getMessage());
    }
}
