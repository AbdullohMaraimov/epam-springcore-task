package gym.crm.controller;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainingRequest;
import gym.crm.model.TrainingType;
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

class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        TrainingRequest request = new TrainingRequest("traineeId", "trainerId", "Bodybuilding", TrainingType.STANDARD, LocalDate.now(), Duration.ofHours(2));
        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "Training created", true);

        when(trainingService.create(any(TrainingRequest.class))).thenReturn(apiResponse);

        ApiResponse<Void> response = trainingController.create(request);

        assertEquals(204, response.statusCode());
        assertEquals("Training created", response.message());
        verify(trainingService, times(1)).create(any(TrainingRequest.class));
    }

    @Test
    void findById() {
        String id = "training123";
        TrainingResponse trainingResponse = new TrainingResponse("training123", "trainee1", "trainer1", "Bodybuilding", TrainingType.STANDARD, LocalDate.now(), Duration.ofHours(2));
        ApiResponse<TrainingResponse> apiResponse = new ApiResponse<>(200, trainingResponse, "Training found", true);

        when(trainingService.findById(id)).thenReturn(apiResponse);

        ApiResponse<TrainingResponse> response = trainingController.findById(id);

        assertEquals(200, response.statusCode());
        assertEquals("Training found", response.message());
        assertEquals(trainingResponse, response.data());
        verify(trainingService, times(1)).findById(id);
    }

    @Test
    void findAll() {
        List<TrainingResponse> trainings = List.of(
                new TrainingResponse("training123", "trainee1", "trainer1", "Bodybuilding", TrainingType.STANDARD, LocalDate.now(), Duration.ofHours(2)),
                new TrainingResponse("training124", "trainee2", "trainer2", "Bodybuilding", TrainingType.STANDARD, LocalDate.now(), Duration.ofHours(1))
        );

        ApiResponse<List<TrainingResponse>> apiResponse = new ApiResponse<>(200, trainings, "Trainings found", true);

        when(trainingService.findAll()).thenReturn(apiResponse);

        ApiResponse<List<TrainingResponse>> response = trainingController.findAll();

        assertEquals(200, response.statusCode());
        assertEquals("Trainings found", response.message());
        assertEquals(trainings, response.data());
        verify(trainingService, times(1)).findAll();
    }
}