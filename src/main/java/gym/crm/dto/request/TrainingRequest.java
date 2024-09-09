package gym.crm.dto.request;

import gym.crm.model.TrainingType;
import jakarta.validation.constraints.*;

import java.time.Duration;
import java.time.LocalDate;

public record TrainingRequest(
        @NotNull(message = "Trainee ID cannot be null")
        @Positive(message = "Trainee ID must be positive")
        Long traineeId,

        @NotNull(message = "Trainer ID cannot be null")
        @Positive(message = "Trainer ID must be positive")
        Long trainerId,

        @NotBlank(message = "Training name cannot be blank")
        @Size(max = 30,message = "Training name should be at most 30 character long")
        String trainingName,

        @NotNull(message = "Training type cannot be null")
        Long trainingTypeId,

        @NotNull(message = "Training date cannot be null")
        @FutureOrPresent(message = "Training date must be in the future or today")
        LocalDate trainingDate,

        @NotNull(message = "Duration cannot be null")
        Duration duration
) {}
