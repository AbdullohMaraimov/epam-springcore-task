package gym_crm.dto.request;

import gym_crm.model.TrainingType;

import java.time.Duration;
import java.time.LocalDate;

public record TrainingRequest(
        String traineeId,
        String trainerId,
        String trainingName,
        TrainingType trainingType,
        LocalDate trainingDate,
        Duration duration
) {}
