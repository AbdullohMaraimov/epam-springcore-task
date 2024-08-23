package gym_crm.dto.reponse;

public record ApiResponse<T> (
        boolean success,
        T data,
        String message
) {}
