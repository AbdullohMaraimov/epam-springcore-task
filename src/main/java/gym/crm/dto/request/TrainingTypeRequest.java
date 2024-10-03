package gym.crm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TrainingTypeRequest(
        @NotNull(message = "Training type name can not be null")
        @NotBlank(message = "Training type name can not be blank")
        String name
) {
}
