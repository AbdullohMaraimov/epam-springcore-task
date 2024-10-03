package gym.crm.dto.reponse;

public record ApiResponse<T> (
        int statusCode,
        boolean success,
        T data,
        String message
) {

    public ApiResponse(int statusCode, String message, boolean status) {
        this(statusCode, status, null, message);
    }
}
