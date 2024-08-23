package gym_crm.dto.request;

import java.time.LocalDate;

public record TraineeRequest(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address,
        boolean isActive
) {}
