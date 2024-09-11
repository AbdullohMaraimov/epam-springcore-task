package gym.crm.controller;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.dto.request.UserLoginRequest;
import gym.crm.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerTrainee() throws IOException {
        TraineeRequest traineeRequest = new TraineeRequest("Iman", "Gadzhi", LocalDate.of(2000, 1, 1), "USA", true);
        ApiResponse<Void> apiResponse = new ApiResponse<>(201, "Trainee registered successfully", true);

        when(authService.register(traineeRequest)).thenReturn(apiResponse);

        ApiResponse<Void> response = authController.register(traineeRequest);

        assertEquals(201, response.statusCode());
        assertEquals("Trainee registered successfully", response.message());
        verify(authService, times(1)).register(traineeRequest);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void registerTrainer() throws IOException {
        TrainerRequest trainerRequest = new TrainerRequest("John", "Doe", 1L);
        ApiResponse<Void> apiResponse = new ApiResponse<>(201, "Trainer registered successfully", true);

        when(authService.register(trainerRequest)).thenReturn(apiResponse);

        ApiResponse<Void> response = authController.register(trainerRequest);

        assertEquals(201, response.statusCode());
        assertEquals("Trainer registered successfully", response.message());
        verify(authService, times(1)).register(trainerRequest);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void login() {
        UserLoginRequest loginRequest = new UserLoginRequest("Iman.Gadzhi", "password123");
        String token = "jwt-token";

        when(authService.login(loginRequest)).thenReturn(token);

        String response = authController.login(loginRequest);

        assertEquals(token, response);
        verify(authService, times(1)).login(loginRequest);
        verifyNoMoreInteractions(authService);
    }

}