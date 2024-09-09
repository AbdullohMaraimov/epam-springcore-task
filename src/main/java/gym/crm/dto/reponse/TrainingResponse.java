package gym.crm.dto.reponse;

import gym.crm.model.TrainingType;

import java.time.Duration;
import java.time.LocalDate;

public record TrainingResponse(
        Long trainingId,
        Long traineeId,
        Long trainerId,
        String trainingName,
        String trainingTypeName,
        LocalDate trainingDate,
        Duration duration
) {}
