package gym.crm.mapper;

import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.model.Trainer;
import gym.crm.model.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrainerMapperTest {

    @Mock
    private TraineeMapper traineeMapper;

    @InjectMocks
    private TrainerMapper trainerMapper;

    @Test
    void toTrainer() {
        TrainerRequest trainerRequest = new TrainerRequest(
                "Iman",
                "Gadzhi",
                1L,
                true
        );

        Trainer trainer = trainerMapper.toTrainer(trainerRequest);

        assertEquals(trainerRequest.firstName(), trainer.getFirstName());
        assertEquals(trainerRequest.lastName(), trainer.getLastName());
        assertEquals("Iman.Gadzhi", trainer.getUsername());
    }

    @Test
    void toTrainerResponse() {
        List<Long> traineeIds = new ArrayList<>();
        traineeIds.add(1L);
        traineeIds.add(2L);

        Trainer trainer = new Trainer();
        trainer.setFirstName("Iman");
        trainer.setLastName("Gadzhi");
        trainer.setUsername("Iman.Gadzhi");
        trainer.setSpecialization(new TrainingType(1L, "GYM"));
        trainer.setIsActive(true);

        TrainerResponse trainerResponse = trainerMapper.toTrainerResponse(trainer);

        assertEquals(trainer.getFirstName(), trainerResponse.firstName());
        assertEquals(trainer.getLastName(), trainerResponse.lastName());
        assertEquals(trainer.getSpecialization().getName(), trainerResponse.specialization());
        assertEquals(trainer.getIsActive(), trainerResponse.isActive());
    }

    @Test
    void toTrainerResponses() {
        List<Trainer> trainers = List.of(
                Trainer.builder()
                        .id(1L)
                        .firstName("Iman")
                        .lastName("Gadzhi")
                        .specialization(new TrainingType(1L, "Fitness"))
                        .isActive(true)
                        .trainees(List.of())
                        .build(),

                Trainer.builder()
                        .id(2L)
                        .firstName("Jim")
                        .lastName("Rohn")
                        .specialization(new TrainingType(2L, "Motivation"))
                        .isActive(true)
                        .trainees(List.of())
                        .build()
        );

        List<TrainerResponse> trainerResponses = trainerMapper.toTrainerResponses(trainers);

        assertEquals(trainers.size(), trainerResponses.size());
    }

    @Test
    void toUpdatedTrainer() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("Iman");
        trainer.setLastName("Gadzhi");
        trainer.setSpecialization(new TrainingType(3L, "GYM"));

        TrainerRequest updatedRequest = new TrainerRequest(
                "John",
                "Doe",
                3L,
                true
        );

        Trainer updatedTrainer = trainerMapper.toUpdatedTrainer(trainer, updatedRequest);

        assertEquals("John", updatedTrainer.getFirstName());
        assertEquals("Doe", updatedTrainer.getLastName());
        assertEquals("GYM", updatedTrainer.getSpecialization().getName());
    }}