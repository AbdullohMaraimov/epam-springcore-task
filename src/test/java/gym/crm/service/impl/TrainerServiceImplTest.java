package gym.crm.service.impl;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.mapper.TrainerMapper;
import gym.crm.model.Trainer;
import gym.crm.repository.TrainerRepository;
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
    private TrainerRepository trainerRepository;

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
        when(trainerRepository.isUsernameExists("alivali")).thenReturn(true);

        ApiResponse<Void> response = trainerService.create(trainerRequest);

        assertEquals("Saved successfully!", response.message());
        assertEquals("alivali1", trainer.getUsername());

        verify(trainerMapper, times(1)).toTrainer(trainerRequest);
        verify(trainerRepository, times(1)).isUsernameExists(anyString());
        verify(trainerRepository, times(1)).save(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerRepository);
    }

    @Test
    void testUsername_Success() {
        TrainerRequest trainerRequest = new TrainerRequest("ali", "vali", 1L);
        Trainer trainer = new Trainer();
        trainer.setUsername("ali.vali");

        when(trainerMapper.toTrainer(trainerRequest)).thenReturn(trainer);
        when(trainerRepository.isUsernameExists("ali.vali")).thenReturn(false);

        ApiResponse<Void> response = trainerService.create(trainerRequest);

        assertEquals("Saved successfully!", response.message());
        verify(trainerMapper, times(1)).toTrainer(trainerRequest);
        verify(trainerRepository, times(1)).isUsernameExists("ali.vali");
        verify(trainerRepository, times(1)).save(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerRepository);
    }

    @Test
    void createTrainer_SuccessfulTest() {
        TrainerRequest trainerRequest = new TrainerRequest("jakie", "chan", 1L);
        Trainer trainer = new Trainer();

        when(trainerMapper.toTrainer(trainerRequest)).thenReturn(trainer);
        when(trainerRepository.isUsernameExists(trainer.getUsername())).thenReturn(false);

        ApiResponse<Void> response = trainerService.create(trainerRequest);

        assertEquals("Saved successfully!", response.message());

        verify(trainerMapper, times(1)).toTrainer(trainerRequest);
        verify(trainerRepository, times(1)).isUsernameExists(trainer.getUsername());
        verify(trainerRepository, times(1)).save(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerRepository);
    }

    @Test
    void updateFails() {
        TrainerRequest trainerRequest = new TrainerRequest("jakie", "chan", 1L);
        Trainer trainer = new Trainer();
        trainer.setUsername("jakie.chan");

        when(trainerRepository.findByUsername(trainer.getUsername())).thenReturn(null);

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            trainerService.update(trainer.getUsername(), trainerRequest);
        });

        assertEquals("Trainer not found!", exception.getMessage());

        verify(trainerRepository, times(1)).findByUsername(trainer.getUsername());
        verify(trainerRepository, never()).update(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerRepository);
    }

    @Test
    void findByUsername_Fails() {
        when(trainerRepository.findByUsername("username")).thenReturn(null);
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            trainerService.findByUsername("username");
        });

        assertEquals("Trainer not found!", exception.getMessage());
        verify(trainerRepository, times(1)).findByUsername("username");
        verifyNoMoreInteractions(trainerMapper, trainerRepository);
    }

    @Test
    void findByUsername_Success() {
        String username = "username";
        Trainer trainer = new Trainer();
        TrainerResponse trainerResponse = new TrainerResponse(1L, "b", "c", "f", "a", true);

        when(trainerRepository.findByUsername(username)).thenReturn(trainer);
        when(trainerMapper.toTrainerResponse(trainer)).thenReturn(trainerResponse);

        ApiResponse<TrainerResponse> response = trainerService.findByUsername(username);

        assertEquals("Successfully found!", response.message());
        assertEquals(trainerResponse, response.data());

        verify(trainerRepository, times(1)).findByUsername(username);
        verify(trainerMapper, times(1)).toTrainerResponse(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerRepository);
    }

    @Test
    void findAll_Fails() {
        when(trainerRepository.findAll()).thenReturn(Collections.emptyList());
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> trainerService.findAll());
        assertEquals("Trainers not found!",  exception.getMessage());
        verify(trainerRepository, times(1)).findAll();
        verifyNoMoreInteractions(trainerMapper, trainerRepository);
    }

    @Test
    void findAllSuccess() {
        Trainer trainer1 = new Trainer();
        Trainer trainer2 = new Trainer();

        TrainerResponse trainerResponse1 = new TrainerResponse(1L, "b", "c", "f", "a", true);
        TrainerResponse trainerResponse2 = new TrainerResponse(1L, "b", "c", "f", "a", true);

        List<Trainer> trainers = List.of(trainer1, trainer2);
        List<TrainerResponse> trainerResponses = List.of(trainerResponse1, trainerResponse2);

        when(trainerRepository.findAll()).thenReturn(trainers);
        when(trainerMapper.toTrainerResponses(trainers)).thenReturn(trainerResponses);

        ApiResponse<List<TrainerResponse>> response = trainerService.findAll();

        assertEquals(trainerResponses, response.data());
        assertEquals("Success!", response.message());

        verify(trainerRepository, times(1)).findAll();
        verify(trainerMapper, times(1)).toTrainerResponses(trainers);
        verifyNoMoreInteractions(trainerMapper, trainerRepository);
    }

    @Test
    public void testUpdatePassword_Success() {
        String username = "JohnDoe";
        String oldPassword = "oldPass";
        String newPassword = "newPass";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setPassword(oldPassword);

        when(trainerRepository.findByUsername(username)).thenReturn(trainer);

        ApiResponse<Void> response = trainerService.updatePassword(username, oldPassword, newPassword);

        assertEquals(200, response.statusCode());
        assertEquals("Password update successful", response.message());
        assertTrue(response.success());

        verify(trainerRepository, times(1)).findByUsername(username);
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    public void testDeActivateUser_Success() {
        String username = "JohnDoe";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setIsActive(true);

        when(trainerRepository.findByUsername(username)).thenReturn(trainer);

        ApiResponse<Void> response = trainerService.deActivateUser(username);

        assertEquals(true, response.success());
        assertEquals("User deActivated successfully", response.message());
        assertTrue(response.success());

        verify(trainerRepository, times(1)).findByUsername(username);
        verify(trainerRepository, times(1)).save(trainer);
    }
}