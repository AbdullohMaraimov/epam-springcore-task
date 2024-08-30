package gym.crm.controller;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TraineeResponse;
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
    void create() {
        TraineeRequest request = new TraineeRequest("Iman", "Gadzhi", LocalDate.of(2000, 1, 1), "USA", true);
        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "Trainee created", true);

        when(traineeService.create(any(TraineeRequest.class))).thenReturn(apiResponse);

        ApiResponse<Void> response = traineeController.create(request);

        assertEquals(204, response.statusCode());
        assertEquals("Trainee created", response.message());
        verify(traineeService, times(1)).create(any(TraineeRequest.class));
    }

    @Test
    void update() {
        String username = "Iman.Gadzhi";
        TraineeRequest request = new TraineeRequest("Iman", "Gadzhi", LocalDate.of(2000, 1, 1), "USA", true);
        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "Trainee updated", true);

        when(traineeService.update(eq(username), any(TraineeRequest.class))).thenReturn(apiResponse);

        ApiResponse<Void> response = traineeController.update(username, request);

        assertEquals(204, response.statusCode());
        assertEquals("Trainee updated", response.message());

        verify(traineeService, times(1)).update(eq(username), any(TraineeRequest.class));
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    void findByUsername() {
        String username = "Iman.Gadzhi";
        TraineeResponse traineeResponse = new TraineeResponse("id", "Iman", "Gadzhi", "Iman.Gadzhi", LocalDate.of(2000, 1, 1), "USA", true);
        ApiResponse<TraineeResponse> apiResponse = new ApiResponse<>(200, traineeResponse, "Trainee found", true);

        when(traineeService.findByUsername(username)).thenReturn(apiResponse);

        ApiResponse<TraineeResponse> response = traineeController.findByUsername(username);

        assertEquals(200, response.statusCode());
        assertEquals("Trainee found", response.message());
        assertEquals(traineeResponse, response.data());
        verify(traineeService, times(1)).findByUsername(username);
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    void findAll() {
        List<TraineeResponse> trainees = List.of(
                new TraineeResponse("id", "Iman", "Gadzhi", "Iman.Gadzhi", LocalDate.of(2000, 1, 1), "USA", true),
                new TraineeResponse("id", "Ali", "Vali", "Ali.Vali", LocalDate.of(2002, 2, 2), "USA", true)
        );

        ApiResponse<List<TraineeResponse>> apiResponse = new ApiResponse<>(200, trainees, "Trainees found", true);

        when(traineeService.findAll()).thenReturn(apiResponse);

        ApiResponse<List<TraineeResponse>> response = traineeController.findAll();

        assertEquals(200, response.statusCode());
        assertEquals("Trainees found", response.message());
        assertEquals(trainees, response.data());
        verify(traineeService, times(1)).findAll();
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    void deleteByUsername() {
        String username = "Iman.Gadzhi";
        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "Deleted successfully!", true);

        when(traineeService.delete(username)).thenReturn(apiResponse);

        ApiResponse<Void> response = traineeController.deleteByUsername(username);

        assertEquals(204, response.statusCode());
        assertEquals("Deleted successfully!", response.message());
        verify(traineeService, times(1)).delete(username);
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    void deleteAll() {
        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "All trainees deleted", true);

        when(traineeService.deleteAll()).thenReturn(apiResponse);

        ApiResponse<Void> response = traineeController.deleteAll();

        assertEquals(204, response.statusCode());
        assertEquals("All trainees deleted", response.message());
        verify(traineeService, times(1)).deleteAll();
        verifyNoMoreInteractions(traineeService);
    }
}