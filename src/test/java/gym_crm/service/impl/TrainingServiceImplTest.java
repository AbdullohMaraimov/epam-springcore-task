package gym_crm.service.impl;

import gym_crm.dto.reponse.ApiResponse;
import gym_crm.dto.reponse.TrainingResponse;
import gym_crm.dto.request.TrainingRequest;
import gym_crm.mapper.TrainingMapper;
import gym_crm.model.Training;
import gym_crm.model.TrainingType;
import gym_crm.repository.TraineeDAO;
import gym_crm.repository.TrainerDAO;
import gym_crm.repository.TrainingDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainingServiceImplTest {

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TrainingMapper trainingMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    void createTrainingSuccessfully() {
        TrainingRequest trainingRequest = new TrainingRequest("trainee1", "trainer1", "training1", TrainingType.STANDARD, LocalDate.now(), Duration.ZERO);
        Training training = new Training();
        when(trainerDAO.isUsernameExists("trainer1")).thenReturn(true);
        when(traineeDAO.isUsernameExists("trainee1")).thenReturn(true);
        when(trainingMapper.toEntity(trainingRequest)).thenReturn(training);

        ApiResponse<Void> response = trainingService.create(trainingRequest);

        assertEquals(true, response.success());
        assertEquals("Training created successfully!", response.message());
        verify(trainingDAO).save(training);
    }

    @Test
    void createTrainingFailsTrainerDoestExist() {
        TrainingRequest trainingRequest = new TrainingRequest("trainee1", "trainer1", "training1", TrainingType.STANDARD, LocalDate.now(), Duration.ZERO);
        when(trainerDAO.isUsernameExists("trainer1")).thenReturn(false);
        when(traineeDAO.isUsernameExists("trainee1")).thenReturn(true);

        ApiResponse<Void> response = trainingService.create(trainingRequest);

        assertEquals(false, response.success());
        assertEquals("Trainer or Trainee does not exist!", response.message());
        verify(trainingDAO, never()).save(any());
    }

    @Test
    void findByIdFails() {
        when (trainingDAO.isTrainingExist("id")).thenReturn(false);
        ApiResponse<TrainingResponse> response = trainingService.findById("id");
        assertEquals("Training not found!", response.message());
    }

    @Test
    void findByIdSuccess() {
        when(trainingDAO.isTrainingExist("id")).thenReturn(true);
        ApiResponse<TrainingResponse> response = trainingService.findById("id");
        assertEquals("Successfully found", response.message());
    }

    @Test
    void findAllTrainingsFails() {
        when(trainingDAO.isTrainingDBEmpty()).thenReturn(true);
        ApiResponse<List<TrainingResponse>> response = trainingService.findAll();
        assertEquals("No Training found!", response.message());
    }

    @Test
    void findAll() {
        List<Training> trainings = List.of(new Training(), new Training());
        List<TrainingResponse> trainingResponses = List.of(
                new TrainingResponse("", "", "", "", TrainingType.STANDARD, LocalDate.now(), Duration.ZERO),
                new TrainingResponse("", "", "", "", TrainingType.STANDARD, LocalDate.now(), Duration.ZERO)
        );

        when(trainingDAO.isTrainingDBEmpty()).thenReturn(false);
        when(trainingDAO.findAll()).thenReturn(trainings);
        when(trainingMapper.toResponses(trainings)).thenReturn(trainingResponses);

        ApiResponse<List<TrainingResponse>> response = trainingService.findAll();

        assertEquals(true, response.success());
        assertEquals("Success!", response.message());
        assertEquals(trainingResponses, response.data());
    }
}