package gym.crm.dto.reponse;

import java.time.LocalDate;
import java.util.List;

public record TraineeResponse(
        Long userId,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address,
        boolean isActive,
        List<TrainerResponse> trainers
) {}
