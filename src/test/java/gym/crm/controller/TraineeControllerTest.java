package gym.crm.controller;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TraineeResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.service.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private TraineeController traineeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void update() {
        String username = "Iman.Gadzhi";
        TraineeRequest request = new TraineeRequest("Iman", "Gadzhi", LocalDate.of(2000, 1, 1), "USA", true);
        TraineeResponse traineeResponse = new TraineeResponse(1L, "Iman", "Gadzhi", LocalDate.of(2000, 1, 1), "USA", true, null);

        when(traineeService.update(eq(username), any(TraineeRequest.class))).thenReturn(traineeResponse);

        ApiResponse<TraineeResponse> response = traineeController.update(username, request);

        assertEquals(204, response.statusCode());
        assertEquals("Successfully updated!", response.message());

        verify(traineeService, times(1)).update(eq(username), any(TraineeRequest.class));
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    void findByUsername() {
        String username = "Iman.Gadzhi";
        TraineeResponse traineeResponse = new TraineeResponse(1L, "Iman", "Gadzhi", LocalDate.of(2000, 1, 1), "USA", true, null);

        when(traineeService.findByUsername(username)).thenReturn(traineeResponse);

        ApiResponse<TraineeResponse> response = traineeController.findByUsername(username);

        assertEquals(200, response.statusCode());
        assertEquals("Successfully found!", response.message());
        assertEquals(traineeResponse, response.data());
        verify(traineeService, times(1)).findByUsername(username);
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    void findAll() {
        List<TraineeResponse> trainees = List.of(
                new TraineeResponse(1L, "Iman", "Gadzhi", LocalDate.of(2000, 1, 1), "USA", true, null),
                new TraineeResponse(1L, "Ali", "Vali", LocalDate.of(2002, 2, 2), "USA", true, null)
        );

        when(traineeService.findAll()).thenReturn(trainees);

        ApiResponse<List<TraineeResponse>> response = traineeController.findAll();

        assertEquals(200, response.statusCode());
        assertEquals("Success!", response.message());
        assertEquals(trainees, response.data());
        verify(traineeService, times(1)).findAll();
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    void deleteByUsername() {
        String username = "Iman.Gadzhi";

        ApiResponse<Void> response = traineeController.deleteByUsername(username);

        assertEquals(200, response.statusCode());
        assertEquals("Deleted successfully!", response.message());
        verify(traineeService, times(1)).delete(username);
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    void deleteAll() {
        ApiResponse<Void> response = traineeController.deleteAll();

        assertEquals(204, response.statusCode());
        assertEquals("All Trainees deleted!", response.message());
        verify(traineeService, times(1)).deleteAll();
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    void deActivateUser() {
        String username = "Iman.Gadzhi";

        ApiResponse<Void> response = traineeController.deActivateUser(username);

        assertEquals(200, response.statusCode());
        assertEquals("User deActivated successfully", response.message());
        verify(traineeService, times(1)).deActivateUser(username);
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    void activateUser() {
        String username = "Iman.Gadzhi";
        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "User activated", true);

        ApiResponse<Void> response = traineeController.activateUser(username);

        assertEquals(200, response.statusCode());
        assertEquals("User deActivated successfully", response.message());
        verify(traineeService, times(1)).activateUser(username);
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    void getAllUnassignedTrainers() {
        String username = "Iman.Gadzhi";
        List<TrainerResponse> trainers = List.of(
                new TrainerResponse(1L, "John", "Doe", "GYM", true, null),
                new TrainerResponse(2L, "Jane", "Doe", "GYM", true, null)
        );

        when(traineeService.findAllUnassignedTrainers(username)).thenReturn(trainers);

        ApiResponse<List<TrainerResponse>> response = traineeController.getAllUnassignedTrainers(username);

        assertEquals(200, response.statusCode());
        assertEquals("Successfully retrieved", response.message());
        assertEquals(trainers, response.data());
        verify(traineeService, times(1)).findAllUnassignedTrainers(username);
        verifyNoMoreInteractions(traineeService);
    }

}