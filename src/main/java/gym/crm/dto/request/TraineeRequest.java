package gym.crm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TraineeRequest(
        @NotBlank(message = "Firstname cannot be blank")
        @NotNull(message = "Firstname cannot be null")
        String firstName,

        @NotBlank(message = "Lastname cannot be blank")
        @NotNull(message = "Lastname cannot be null")
        String lastName,

        LocalDate dateOfBirth,

        String address,

        @NotNull(message = "isActive cannot be null")
        boolean isActive
) {
}
