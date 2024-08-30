package gym.crm.service.impl;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.mapper.TrainerMapper;
import gym.crm.model.Trainer;
import gym.crm.repository.TrainerDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        verify(trainerMapper, times(1)).toTrainer(trainerRequest);
        verify(trainerDAO, times(1)).isUsernameExists(anyString());
        verify(trainerDAO, times(1)).save(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerDAO);
    }

    @Test
    void testUsernameSuccess() {
        TrainerRequest trainerRequest = new TrainerRequest("ali", "vali", "bodybuilder");
        Trainer trainer = new Trainer();
        trainer.setUsername("ali.vali");

        when(trainerMapper.toTrainer(trainerRequest)).thenReturn(trainer);
        when(trainerDAO.isUsernameExists("ali.vali")).thenReturn(false);

        ApiResponse<Void> response = trainerService.create(trainerRequest);

        assertEquals("Saved successfully!", response.message());
        verify(trainerMapper, times(1)).toTrainer(trainerRequest);
        verify(trainerDAO, times(1)).isUsernameExists("ali.vali");
        verify(trainerDAO, times(1)).save(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerDAO);
    }

    @Test
    void createTrainerExistsTest() {
        TrainerRequest trainerRequest = new TrainerRequest("jakie", "chan", "bodybuilder");
        Trainer trainer = new Trainer();
        trainer.setUsername("jakie.chan");

        when(trainerMapper.toTrainer(trainerRequest)).thenReturn(trainer);
        when(trainerDAO.isUsernameExists(trainer.getUsername())).thenReturn(true);

        ApiResponse<Void> response = trainerService.create(trainerRequest);
        assertEquals("Username already exists, so changed it to " + trainer.getUsername(), response.message());

        verify(trainerMapper, times(1)).toTrainer(trainerRequest);
        verify(trainerDAO, times(1)).isUsernameExists("jakie.chan");
        verify(trainerDAO, times(1)).save(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerDAO);
    }
    @Test
    void createTrainerSuccessfulTest() {
        TrainerRequest trainerRequest = new TrainerRequest("jakie", "chan", "bodybuilder");
        Trainer trainer = new Trainer();

        when(trainerMapper.toTrainer(trainerRequest)).thenReturn(trainer);
        when(trainerDAO.isUsernameExists(trainer.getUsername())).thenReturn(false);

        ApiResponse<Void> response = trainerService.create(trainerRequest);

        assertEquals("Saved successfully!", response.message());

        verify(trainerMapper, times(1)).toTrainer(trainerRequest);
        verify(trainerDAO, times(1)).isUsernameExists(trainer.getUsername());
        verify(trainerDAO, times(1)).save(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerDAO);
    }

    @Test
    void updateFails() {
        TrainerRequest trainerRequest = new TrainerRequest("jakie", "chan", "bodybuilder");
        Trainer trainer = new Trainer();
        trainer.setUsername("jakie.chan");

        when(trainerDAO.isUsernameExists(trainer.getUsername())).thenReturn(false);

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            trainerService.update(trainer.getUsername(), trainerRequest);
        });

        assertEquals("Trainer not found!", exception.getMessage());

        verify(trainerDAO, times(1)).isUsernameExists(trainer.getUsername());
        verify(trainerDAO, never()).update(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerDAO);
    }

    @Test
    void findByUsernameFails() {
        when(trainerDAO.isUsernameExists("username")).thenReturn(false);
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            trainerService.findByUsername("username");
        });

        assertEquals("Trainer not found!", exception.getMessage());
        verify(trainerDAO, times(1)).isUsernameExists("username");
        verifyNoMoreInteractions(trainerMapper, trainerDAO);
    }

    @Test
    void findByUsernameSuccess() {
        String username = "username";
        Trainer trainer = new Trainer();
        TrainerResponse trainerResponse = new TrainerResponse("a", "b", "c", "f", "a", true);

        when(trainerDAO.isUsernameExists(username)).thenReturn(true);
        when(trainerDAO.findByUsername(username)).thenReturn(trainer);
        when(trainerMapper.toTrainerResponse(trainer)).thenReturn(trainerResponse);

        ApiResponse<TrainerResponse> response = trainerService.findByUsername(username);

        assertEquals("Successfully found!", response.message());
        assertEquals(trainerResponse, response.data());

        verify(trainerDAO, times(1)).isUsernameExists(username);
        verify(trainerDAO, times(1)).findByUsername(username);
        verify(trainerMapper, times(1)).toTrainerResponse(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerDAO);
    }

    @Test
    void findAllFails() {
        when(trainerDAO.isTrainerDBEmpty()).thenReturn(true);
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> trainerService.findAll());
        assertEquals("Trainer not found!",  exception.getMessage());
        verify(trainerDAO, times(1)).isTrainerDBEmpty();
        verifyNoMoreInteractions(trainerMapper, trainerDAO);
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

        verify(trainerDAO, times(1)).isTrainerDBEmpty();
        verify(trainerDAO, times(1)).findAll();
        verify(trainerMapper, times(1)).toTrainerResponses(trainers);
        verifyNoMoreInteractions(trainerMapper, trainerDAO);
    }
}