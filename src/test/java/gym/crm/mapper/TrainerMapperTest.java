package gym.crm.mapper;

import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.model.Trainer;
import gym.crm.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrainerMapperTest {

    private TrainerMapper trainerMapper;

    @BeforeEach
    void setUp() {
        trainerMapper = new TrainerMapper();
    }

    @Test
    void toTrainer() {
        TrainerRequest trainerRequest = new TrainerRequest(
                "Iman",
                "Gadzhi",
                1L,
                true
        );

        Trainer trainer = trainerMapper.toTrainer(trainerRequest);

        assertEquals("Iman", trainer.getFirstName());
        assertEquals("Gadzhi", trainer.getLastName());
        assertEquals("Iman.Gadzhi", trainer.getUsername());
    }

    @Test
    void toTrainerResponse() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("Iman");
        trainer.setLastName("Gadzhi");
        trainer.setUsername("Iman.Gadzhi");
        trainer.setSpecialization(new TrainingType(1L, "GYM"));
        trainer.setIsActive(true);

        TrainerResponse response = trainerMapper.toTrainerResponse(trainer);

        assertEquals(trainer.getFirstName(), response.firstName());
        assertEquals(trainer.getLastName(), response.lastName());
        assertEquals(trainer.getSpecialization().getName(), response.specialization());
        assertEquals(trainer.getIsActive(), response.isActive());
    }

    @Test
    void toTrainerResponses() {
        Trainer trainer1 = new Trainer();
        trainer1.setId(1L);
        trainer1.setFirstName("Iman");
        trainer1.setLastName("Gadzhi");
        trainer1.setUsername("Iman.Gadzhi");
        trainer1.setSpecialization(new TrainingType(1L, "GYM"));
        trainer1.setIsActive(true);

        Trainer trainer2 = new Trainer();
        trainer2.setId(1L);
        trainer2.setFirstName("John");
        trainer2.setLastName("Doe");
        trainer2.setUsername("John.Doe");
        trainer2.setSpecialization(new TrainingType(2L, "GYM"));
        trainer2.setIsActive(false);

        List<Trainer> trainers = List.of(trainer1, trainer2);

        List<TrainerResponse> responses = trainerMapper.toTrainerResponses(trainers);

        assertEquals(2, responses.size());
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
    }
}