package gym.crm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TraineeRequest(
        @NotBlank(message = "Firstname cannot be blank")
        @Size(min = 3, max = 30, message = "Firstname should be between 3 and 30 character long")
        String firstName,

        String lastName,

        @NotNull(message = "DOB can not be null")
        @Past(message = "DOB should be in the past")
        LocalDate dateOfBirth,

        @NotBlank(message = "Address can not be blank")
        @Size(min = 2,max = 40, message = "Address should be between 2 and 40 character long")
        String address,

        boolean isActive
) {
}
