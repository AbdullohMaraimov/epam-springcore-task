package gym.crm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TrainerRequest(
        @NotBlank(message = "Firstname cannot be blank")
        @Size(min = 3, max = 30, message = "Firstname should be between 3 and 30 character long")
        String firstName,

        String lastName,

        @NotNull(message = "Specialization cannot be blank")
        @Positive
        Long specializationId
) {}
