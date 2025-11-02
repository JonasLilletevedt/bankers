package no.setup.bankers.http;

import java.util.Map;

public record ApiError(
    String type,
    String message,
    String details,
    Map<String, Object> meta
) {
    public static ApiError of(String type, String message, String details) {
        return new ApiError(type, message, details, Map.of());
    }

    public static ApiError of(String type, String message) {
        return new ApiError(type, message, null, Map.of());
    }
}
