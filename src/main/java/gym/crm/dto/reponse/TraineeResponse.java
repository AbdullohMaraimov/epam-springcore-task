package gym.crm.dto.reponse;

import java.time.LocalDate;

public record TraineeResponse(
        String userId,
        String firstName,
        String lastName,
        String username,
        LocalDate dateOfBirth,
        String address,
        boolean isActive
) {}
