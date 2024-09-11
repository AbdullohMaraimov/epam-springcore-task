package gym.crm.dto.reponse;

public record TrainerResponse(
        Long userId,
        String firstName,
        String lastName,
        String username,
        String specialization,
        boolean isActive
) {
}
