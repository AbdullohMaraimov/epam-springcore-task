package gym_crm.service.impl;

import gym_crm.dto.reponse.ApiResponse;
import gym_crm.dto.reponse.TrainerResponse;
import gym_crm.dto.request.TrainerRequest;
import gym_crm.mapper.TrainerMapper;
import gym_crm.model.Trainer;
import gym_crm.repository.TrainerDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainerServiceImplTest {

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TrainerDAO trainerDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    void testUsernameExistsAddedSuffix() {
        TrainerRequest trainerRequest = new TrainerRequest("ali", "vali", "bodybuilder");
        Trainer trainer = new Trainer();
        trainer.setUsername("alivali");

        when(trainerMapper.toTrainer(trainerRequest)).thenReturn(trainer);
        when(trainerDAO.isUsernameExists("alivali")).thenReturn(true);
        when(trainerDAO.isUsernameExists("alivali0")).thenReturn(false);

        ApiResponse<Void> response = trainerService.create(trainerRequest);

        assertEquals("Username already exists, so changed it to alivali0", response.message());
        assertEquals("alivali0", trainer.getUsername());
        verify(trainerDAO).save(trainer);
    }

    @Test
    void testUsernameSuccess() {
        TrainerRequest trainerRequest = new TrainerRequest("ali", "vali", "bodybuilder");
        Trainer trainer = new Trainer();
        trainer.setUsername("alivali");

        when(trainerMapper.toTrainer(trainerRequest)).thenReturn(trainer);
        when(trainerDAO.isUsernameExists("alivali")).thenReturn(false);

        ApiResponse<Void> response = trainerService.create(trainerRequest);

        assertEquals("Saved successfully!", response.message());
    }

    @Test
    void createTrainerExistsTest() {
        TrainerRequest trainerRequest = new TrainerRequest("jakie", "chan", "bodybuilder");
        Trainer trainer = new Trainer();

        when(trainerMapper.toTrainer(trainerRequest)).thenReturn(trainer);
        when(trainerDAO.isUsernameExists(trainer.getUsername())).thenReturn(true);

        ApiResponse<Void> response = trainerService.create(trainerRequest);
        verify(trainerDAO).save(trainer);
        assertEquals("Username already exists, so changed it to " + trainer.getUsername(), response.message());
    }
    @Test
    void createTrainerSuccessfulTest() {
        TrainerRequest trainerRequest = new TrainerRequest("jakie", "chan", "bodybuilder");
        Trainer trainer = new Trainer();

        when(trainerMapper.toTrainer(trainerRequest)).thenReturn(trainer);
        when(trainerDAO.isUsernameExists(trainer.getUsername())).thenReturn(false);

        ApiResponse<Void> response = trainerService.create(trainerRequest);
        verify(trainerDAO).save(trainer);
        assertEquals("Saved successfully!", response.message());
    }

    @Test
    void updateFails() {
        TrainerRequest trainerRequest = new TrainerRequest("jakie", "chan", "bodybuilder");
        Trainer trainer = new Trainer();
        trainer.setUsername("jakie.chan");

        when(trainerMapper.toUpdatedTrainer(trainer, trainerRequest)).thenReturn(trainer);
        when(trainerDAO.isUsernameExists(trainer.getUsername())).thenReturn(false);

        ApiResponse<Void> response = trainerService.update(trainer.getUsername(), trainerRequest);

        verify(trainerDAO, never()).update(trainer);
        assertEquals("Trainer not found!", response.message());
    }

    @Test
    void findByUsernameFails() {
        when(trainerDAO.isUsernameExists("username")).thenReturn(false);
        ApiResponse<TrainerResponse> response = trainerService.findByUsername("username");
        assertEquals(response.message(), "Trainer not found!");
    }

    @Test
    void findByUsernameSuccess() {
        when(trainerDAO.isUsernameExists("username")).thenReturn(true);
        ApiResponse<TrainerResponse> response = trainerService.findByUsername("username");
        assertEquals(response.message(), "Successfully found!");
    }

    @Test
    void findAllFails() {
        when(trainerDAO.isTrainerDBEmpty()).thenReturn(true);
        ApiResponse<List<TrainerResponse>> response = trainerService.findAll();
        assertEquals(response.message(), "No Trainer found!");
    }

    @Test
    void findAllSuccess() {
        Trainer trainer1 = new Trainer();
        Trainer trainer2 = new Trainer();

        TrainerResponse trainerResponse1 = new TrainerResponse("a", "b", "c", "f", "a", true);
        TrainerResponse trainerResponse2 = new TrainerResponse("a", "b", "c", "f", "a", true);

        List<Trainer> trainers = List.of(trainer1, trainer2);
        List<TrainerResponse> trainerResponses = List.of(trainerResponse1, trainerResponse2);

        when(trainerDAO.isTrainerDBEmpty()).thenReturn(false);
        when(trainerDAO.findAll()).thenReturn(trainers);
        when(trainerMapper.toTrainerResponses(trainers)).thenReturn(trainerResponses);

        ApiResponse<List<TrainerResponse>> response = trainerService.findAll();

        assertEquals(trainerResponses, response.data());
        assertEquals("Success!", response.message());
    }
}