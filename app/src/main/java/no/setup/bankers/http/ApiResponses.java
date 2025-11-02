package no.setup.bankers.http;

import java.net.ConnectException;
import java.util.Map;

import io.javalin.http.Context;

public class ApiResponses {
    private ApiResponses() {}

    public static void badRequest(Context ctx, String details) {
        ctx.status(400).json(ApiError.of(
            "INPUT", 
            "Input Error",
            details
        ));
    }
    
    public static void notFound(Context ctx, String details) {
        ctx.status(400).json(ApiError.of(
            "NOT_FOUND", 
            "Resource not found",
            details
        ));
    }

    public static void serverError(Context ctx, String details) {
        ctx.status(500).json(ApiError.of(
            "SERVER", 
            "Internal Error",
            details
        ));
    }

    public static void fieldError(Context ctx, String field, String details) {
        ctx.status(400).json(new ApiError(
            "INPUT", 
            "Invalid field", 
            details, 
            Map.of("field", field)
        ));
    }

    public static <T> void ok(Context ctx, T data) {
        ctx.status(200).json(ApiSuccess.of(data));
    }
}
