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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    void testUsernameExists_AddedSuffix() {
        TrainerRequest trainerRequest = new TrainerRequest("ali", "vali", 1L);
        Trainer trainer = new Trainer();
        trainer.setUsername("alivali");

        when(trainerMapper.toTrainer(trainerRequest)).thenReturn(trainer);
        when(trainerDAO.isUsernameExists("alivali")).thenReturn(true);

        ApiResponse<Void> response = trainerService.create(trainerRequest);

        assertEquals("Saved successfully!", response.message());
        assertEquals("alivali1", trainer.getUsername());

        verify(trainerMapper, times(1)).toTrainer(trainerRequest);
        verify(trainerDAO, times(1)).isUsernameExists(anyString());
        verify(trainerDAO, times(1)).save(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerDAO);
    }

    @Test
    void testUsername_Success() {
        TrainerRequest trainerRequest = new TrainerRequest("ali", "vali", 1L);
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
    void createTrainer_SuccessfulTest() {
        TrainerRequest trainerRequest = new TrainerRequest("jakie", "chan", 1L);
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
        TrainerRequest trainerRequest = new TrainerRequest("jakie", "chan", 1L);
        Trainer trainer = new Trainer();
        trainer.setUsername("jakie.chan");

        when(trainerDAO.findByUsername(trainer.getUsername())).thenReturn(null);

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            trainerService.update(trainer.getUsername(), trainerRequest);
        });

        assertEquals("Trainer not found!", exception.getMessage());

        verify(trainerDAO, times(1)).findByUsername(trainer.getUsername());
        verify(trainerDAO, never()).update(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerDAO);
    }

    @Test
    void findByUsername_Fails() {
        when(trainerDAO.findByUsername("username")).thenReturn(null);
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            trainerService.findByUsername("username");
        });

        assertEquals("Trainer not found!", exception.getMessage());
        verify(trainerDAO, times(1)).findByUsername("username");
        verifyNoMoreInteractions(trainerMapper, trainerDAO);
    }

    @Test
    void findByUsername_Success() {
        String username = "username";
        Trainer trainer = new Trainer();
        TrainerResponse trainerResponse = new TrainerResponse(1L, "b", "c", "f", "a", true);

        when(trainerDAO.findByUsername(username)).thenReturn(trainer);
        when(trainerMapper.toTrainerResponse(trainer)).thenReturn(trainerResponse);

        ApiResponse<TrainerResponse> response = trainerService.findByUsername(username);

        assertEquals("Successfully found!", response.message());
        assertEquals(trainerResponse, response.data());

        verify(trainerDAO, times(1)).findByUsername(username);
        verify(trainerMapper, times(1)).toTrainerResponse(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerDAO);
    }

    @Test
    void findAll_Fails() {
        when(trainerDAO.findAll()).thenReturn(Collections.emptyList());
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> trainerService.findAll());
        assertEquals("Trainers not found!",  exception.getMessage());
        verify(trainerDAO, times(1)).findAll();
        verifyNoMoreInteractions(trainerMapper, trainerDAO);
    }

    @Test
    void findAllSuccess() {
        Trainer trainer1 = new Trainer();
        Trainer trainer2 = new Trainer();

        TrainerResponse trainerResponse1 = new TrainerResponse(1L, "b", "c", "f", "a", true);
        TrainerResponse trainerResponse2 = new TrainerResponse(1L, "b", "c", "f", "a", true);

        List<Trainer> trainers = List.of(trainer1, trainer2);
        List<TrainerResponse> trainerResponses = List.of(trainerResponse1, trainerResponse2);

        when(trainerDAO.findAll()).thenReturn(trainers);
        when(trainerMapper.toTrainerResponses(trainers)).thenReturn(trainerResponses);

        ApiResponse<List<TrainerResponse>> response = trainerService.findAll();

        assertEquals(trainerResponses, response.data());
        assertEquals("Success!", response.message());

        verify(trainerDAO, times(1)).findAll();
        verify(trainerMapper, times(1)).toTrainerResponses(trainers);
        verifyNoMoreInteractions(trainerMapper, trainerDAO);
    }

    @Test
    public void testUpdatePassword_Success() {
        String username = "JohnDoe";
        String oldPassword = "oldPass";
        String newPassword = "newPass";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setPassword(oldPassword);

        when(trainerDAO.findByUsername(username)).thenReturn(trainer);

        ApiResponse<Void> response = trainerService.updatePassword(username, oldPassword, newPassword);

        assertEquals(200, response.statusCode());
        assertEquals("Password update successful", response.message());
        assertTrue(response.success());

        verify(trainerDAO, times(1)).findByUsername(username);
        verify(trainerDAO, times(1)).save(trainer);
    }

    @Test
    public void testDeActivateUser_Success() {
        String username = "JohnDoe";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setIsActive(true);

        when(trainerDAO.findByUsername(username)).thenReturn(trainer);

        ApiResponse<Void> response = trainerService.deActivateUser(username);

        assertEquals(true, response.success());
        assertEquals("User deActivated successfully", response.message());
        assertTrue(response.success());

        verify(trainerDAO, times(1)).findByUsername(username);
        verify(trainerDAO, times(1)).save(trainer);
    }
}