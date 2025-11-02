package no.setup.bankers.http;

public record ApiSuccess<T>(
    String status,
    T data
) {
    public static <T> ApiSuccess<T> of (T data) {
        return new ApiSuccess<T>("ok", data);
    }
}
    
