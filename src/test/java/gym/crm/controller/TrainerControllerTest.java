package gym.crm.controller;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.service.TrainerService;
import gym.crm.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TrainerControllerTest {

    @Mock
    private TrainingService trainingService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByUsername() {
        String username = "Iman.Gadzhi";
        TrainerResponse trainerResponse = new TrainerResponse(1L, "Iman", "Gadzhi", "GYM", true, null);

        when(trainerService.findByUsername(username)).thenReturn(trainerResponse);

        ApiResponse<TrainerResponse> response = trainerController.findByUsername(username);

        assertEquals(200, response.statusCode());
        assertEquals("Successfully found!", response.message());
        assertEquals(trainerResponse, response.data());

        verify(trainerService, times(1)).findByUsername(username);
        verifyNoMoreInteractions(trainerService);
    }

    @Test
    void findAll() {
        List<TrainerResponse> trainers = List.of(
                new TrainerResponse(1L, "John", "Doe", "Fitness", true, null),
                new TrainerResponse(2L, "Jim", "Rohn", "Yoga", true, null)
        );

        when(trainerService.findAll()).thenReturn(trainers);

        ApiResponse<List<TrainerResponse>> response = trainerController.findAll();

        assertEquals(200, response.statusCode());
        assertEquals("Success!", response.message());
        assertEquals(trainers, response.data());

        verify(trainerService, times(1)).findAll();
        verifyNoMoreInteractions(trainerService);
    }

    @Test
    void update() {
        String username = "Jim.Rohn";
        TrainerRequest request = new TrainerRequest("Jim", "Rohn", 1L, true);
        TrainerResponse trainerResponse = new TrainerResponse(1L, username, "Rohn", "Yoga", true, null);

        when(trainerService.update(eq(username), any(TrainerRequest.class))).thenReturn(trainerResponse);

        ApiResponse<TrainerResponse> response = trainerController.update(username, request);

        assertEquals(200, response.statusCode());
        assertEquals("Successfully updated!", response.message());

        verify(trainerService, times(1)).update(eq(username), any(TrainerRequest.class));
        verifyNoMoreInteractions(trainerService);
    }

    @Test
    void getTrainerTrainings() {
        String username = "John.Doe";
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);
        String traineeName = "Iman";


        List<TrainingResponse> trainings = List.of(
                new TrainingResponse(1L, 1L, 1L, "Iman.Gadzhi", "GYM", LocalDate.of(2023, 5, 1), Duration.ZERO),
                new TrainingResponse(2L, 1L, 1L, "Iman.Gadzhi", "GYM",LocalDate.of(2023, 6, 15), Duration.ZERO)
        );

        when(trainingService.getTrainingsByTrainer(username, fromDate, toDate, traineeName)).thenReturn(trainings);

        ApiResponse<List<TrainingResponse>> response = trainerController.getTrainerTrainings(username, fromDate, toDate, traineeName);

        assertEquals(200, response.statusCode());
        assertEquals("Successfully found!", response.message());
        assertEquals(trainings, response.data());
        verify(trainingService, times(1)).getTrainingsByTrainer(username, fromDate, toDate, traineeName);
        verifyNoMoreInteractions(trainingService);
    }

    @Test
    void updatePassword() {
        String username = "John.Doe";
        String oldPassword = "oldPassword123";
        String newPassword = "newPassword456";

        ApiResponse<Void> response = trainerController.updatePassword(username, oldPassword, newPassword);

        assertEquals(200, response.statusCode());
        assertEquals("Password update successful", response.message());
        verify(trainerService, times(1)).updatePassword(username, oldPassword, newPassword);
        verifyNoMoreInteractions(trainerService);
    }

    @Test
    void deActivateUser() {
        String username = "John.Doe";

        ApiResponse<Void> response = trainerController.deActivateUser(username);

        assertEquals(200, response.statusCode());
        assertEquals("User deActivated successfully", response.message());
        verify(trainerService, times(1)).deActivateUser(username);
        verifyNoMoreInteractions(trainerService);
    }

    @Test
    void activateUser() {
        String username = "John.Doe";

        ApiResponse<Void> response = trainerController.activateUser(username);

        assertEquals(200, response.statusCode());
        assertEquals("User Activated successfully", response.message());
        verify(trainerService, times(1)).activateUser(username);
        verifyNoMoreInteractions(trainerService);
    }

}