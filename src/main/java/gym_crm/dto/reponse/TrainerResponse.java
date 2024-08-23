package gym_crm.dto.reponse;

public record TrainerResponse(
        String userId,
        String firstName,
        String lastName,
        String username,
        String specialization,
        boolean isActive
) {
}
