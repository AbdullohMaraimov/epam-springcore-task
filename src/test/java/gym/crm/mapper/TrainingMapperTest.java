package gym.crm.mapper;

import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainingRequest;
import gym.crm.model.Training;
import gym.crm.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TrainingMapperTest {

    private TrainingMapper trainingMapper;

    @BeforeEach
    void setUp() {
        trainingMapper = new TrainingMapper();
    }

    @Test
    void toEntity() {
        TrainingRequest request = new TrainingRequest(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "Strength Training",
                TrainingType.STANDARD,
                LocalDate.of(2024, 11, 11),
                Duration.ZERO
        );

        Training training = trainingMapper.toEntity(request);

        assertNotNull(training.getId());
        assertEquals(request.traineeId(), training.getTraineeId());
        assertEquals(request.trainerId(), training.getTrainerId());
        assertEquals(request.trainingName(), training.getTrainingName());
        assertEquals(request.trainingType(), training.getTrainingType());
        assertEquals(request.trainingDate(), training.getTrainingDate());
        assertEquals(request.duration(), training.getDuration());
    }

    @Test
    void toResponse() {
        Training training = new Training();
        training.setId(UUID.randomUUID().toString());
        training.setTraineeId(UUID.randomUUID().toString());
        training.setTrainerId(UUID.randomUUID().toString());
        training.setTrainingName("Strength Training");
        training.setTrainingType(TrainingType.STANDARD);
        training.setTrainingDate(LocalDate.of(2024, 11, 11));
        training.setDuration(Duration.ZERO);

        TrainingResponse response = trainingMapper.toResponse(training);

        assertEquals(training.getId(), response.trainingId());
        assertEquals(training.getTraineeId(), response.traineeId());
        assertEquals(training.getTrainerId(), response.trainerId());
        assertEquals(training.getTrainingName(), response.trainingName());
        assertEquals(training.getTrainingType(), response.trainingType());
        assertEquals(training.getTrainingDate(), response.trainingDate());
        assertEquals(training.getDuration(), response.duration());
    }

    @Test
    void toResponses() {
        List<Training> trainings = List.of(
                new Training(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                        "Strength Training", TrainingType.HARD, LocalDate.of(2023, 8, 28), Duration.ZERO),

                new Training(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                        "Cardio", TrainingType.HARD, LocalDate.of(2023, 8, 29), Duration.ZERO)
        );

        List<TrainingResponse> responses = trainingMapper.toResponses(trainings);

        assertEquals(trainings.size(), responses.size());
        for (int i = 0; i < trainings.size(); i++) {
            Training training = trainings.get(i);
            TrainingResponse response = responses.get(i);

            assertEquals(training.getId(), response.trainingId());
            assertEquals(training.getTraineeId(), response.traineeId());
            assertEquals(training.getTrainerId(), response.trainerId());
            assertEquals(training.getTrainingName(), response.trainingName());
            assertEquals(training.getTrainingType(), response.trainingType());
            assertEquals(training.getTrainingDate(), response.trainingDate());
            assertEquals(training.getDuration(), response.duration());
        }
    }
}