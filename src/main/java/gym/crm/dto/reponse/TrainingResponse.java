package gym.crm.dto.reponse;

import gym.crm.model.TrainingType;

import java.time.Duration;
import java.time.LocalDate;

public record TrainingResponse(
        String trainingId,
        String traineeId,
        String trainerId,
        String trainingName,
        TrainingType trainingType,
        LocalDate trainingDate,
        Duration duration
) {}
