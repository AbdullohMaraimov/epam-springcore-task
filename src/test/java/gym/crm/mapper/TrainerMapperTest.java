//package gym.crm.mapper;
//
//import gym.crm.dto.reponse.TrainerResponse;
//import gym.crm.dto.request.TrainerRequest;
//import gym.crm.model.Trainer;
//import gym.crm.util.PasswordGenerator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mockStatic;
//
//class TrainerMapperTest {
//
//    private TrainerMapper trainerMapper;
//
//    @BeforeEach
//    void setUp() {
//        trainerMapper = new TrainerMapper();
//    }
//
//    @Test
//    void toTrainer() {
//        MockedStatic<PasswordGenerator> passwordGeneratorMockedStatic = mockStatic(PasswordGenerator.class);
//
//        passwordGeneratorMockedStatic.when(PasswordGenerator::generatePassword).thenReturn("password");
//
//        TrainerRequest trainerRequest = new TrainerRequest(
//                "Iman",
//                "Gadzhi",
//                "GYM"
//        );
//
//        Trainer trainer = trainerMapper.toTrainer(trainerRequest);
//
//        assertEquals("Iman", trainer.getFirstName());
//        assertEquals("Gadzhi", trainer.getLastName());
//        assertEquals("GYM", trainer.getSpecialization());
//        assertEquals("password", trainer.getPassword());
//        assertEquals("Iman.Gadzhi", trainer.getUsername());
//    }
//
//    @Test
//    void toTrainerResponse() {
//        Trainer trainer = new Trainer();
//        trainer.setUserId(UUID.randomUUID().toString());
//        trainer.setFirstName("Iman");
//        trainer.setLastName("Gadzhi");
//        trainer.setUsername("Iman.Gadzhi");
//        trainer.setSpecialization("GYM");
//        trainer.setIsActive(true);
//
//        TrainerResponse response = trainerMapper.toTrainerResponse(trainer);
//
//        assertEquals(trainer.getUserId(), response.userId());
//        assertEquals(trainer.getFirstName(), response.firstName());
//        assertEquals(trainer.getLastName(), response.lastName());
//        assertEquals(trainer.getUsername(), response.username());
//        assertEquals(trainer.getSpecialization(), response.specialization());
//        assertEquals(trainer.getIsActive(), response.isActive());
//    }
//
//    @Test
//    void toTrainerResponses() {
//        Trainer trainer1 = new Trainer();
//        trainer1.setUserId(UUID.randomUUID().toString());
//        trainer1.setFirstName("Iman");
//        trainer1.setLastName("Gadzhi");
//        trainer1.setUsername("Iman.Gadzhi");
//        trainer1.setSpecialization("GYM");
//        trainer1.setIsActive(true);
//
//        Trainer trainer2 = new Trainer();
//        trainer2.setUserId(UUID.randomUUID().toString());
//        trainer2.setFirstName("John");
//        trainer2.setLastName("Doe");
//        trainer2.setUsername("John.Doe");
//        trainer2.setSpecialization("Fitness");
//        trainer2.setIsActive(false);
//
//        List<Trainer> trainers = List.of(trainer1, trainer2);
//
//        List<TrainerResponse> responses = trainerMapper.toTrainerResponses(trainers);
//
//        assertEquals(2, responses.size());
//    }
//
//    @Test
//    void toUpdatedTrainer() {
//        Trainer trainer = new Trainer();
//        trainer.setFirstName("Iman");
//        trainer.setLastName("Gadzhi");
//        trainer.setSpecialization("GYM");
//
//        TrainerRequest updatedRequest = new TrainerRequest(
//                "John",
//                "Doe",
//                "Fitness"
//        );
//
//        Trainer updatedTrainer = trainerMapper.toUpdatedTrainer(trainer, updatedRequest);
//
//        assertEquals("John", updatedTrainer.getFirstName());
//        assertEquals("Doe", updatedTrainer.getLastName());
//        assertEquals("Fitness", updatedTrainer.getSpecialization());
//    }
//}