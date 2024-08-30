package gym.crm.service.impl;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainingRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.mapper.TrainingMapper;
import gym.crm.model.Training;
import gym.crm.model.TrainingType;
import gym.crm.repository.TraineeDAO;
import gym.crm.repository.TrainerDAO;
import gym.crm.repository.TrainingDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        verify(trainerDAO, times(1)).isUsernameExists("trainer1");
        verify(traineeDAO, times(1)).isUsernameExists("trainee1");
        verify(trainingMapper, times(1)).toEntity(trainingRequest);
        verify(trainingDAO, times(1)).save(training);
        verifyNoMoreInteractions(trainerDAO, traineeDAO, trainingMapper, trainingDAO);
    }

    @Test
    void createTrainingFailsTrainerDoestExist() {
        TrainingRequest trainingRequest = new TrainingRequest("trainee1", "trainer1", "training1", TrainingType.STANDARD, LocalDate.now(), Duration.ZERO);

        when(trainerDAO.isUsernameExists("trainer1")).thenReturn(false);
        when(traineeDAO.isUsernameExists("trainee1")).thenReturn(true);

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            trainingService.create(trainingRequest);
        });

        assertEquals("Trainer or Trainee does not exist!", exception.getMessage());

        verify(trainerDAO, times(1)).isUsernameExists("trainer1");
        verify(traineeDAO, times(0)).isUsernameExists("trainee1");
        verify(trainingDAO, never()).save(any());
        verifyNoMoreInteractions(trainerDAO, traineeDAO, trainingDAO);
    }

    @Test
    void findByIdFails() {
        when(trainingDAO.isTrainingExist("id")).thenReturn(false);
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            trainingService.findById("id");
        });
        assertEquals("Training not found!", exception.getMessage());

        verify(trainingDAO, times(1)).isTrainingExist("id");
        verifyNoMoreInteractions(trainingDAO);
    }

    @Test
    void findByIdSuccess() {
        String trainingId = "id";
        Training training = new Training();
        TrainingResponse trainingResponse = new TrainingResponse(
                "", "", "", "", TrainingType.STANDARD, LocalDate.now(), Duration.ZERO
        );

        when(trainingDAO.isTrainingExist(trainingId)).thenReturn(true);
        when(trainingDAO.findById(trainingId)).thenReturn(training);
        when(trainingMapper.toResponse(training)).thenReturn(trainingResponse);

        ApiResponse<TrainingResponse> response = trainingService.findById(trainingId);

        assertEquals("Successfully found", response.message());
        assertEquals(trainingResponse, response.data());

        verify(trainingDAO, times(1)).isTrainingExist(trainingId);
        verify(trainingDAO, times(1)).findById(trainingId);
        verify(trainingMapper, times(1)).toResponse(training);
        verifyNoMoreInteractions(trainingDAO, trainingMapper);
    }

    @Test
    void findAllTrainingsFails() {
        when(trainingDAO.isTrainingDBEmpty()).thenReturn(true);
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> trainingService.findAll());
        assertEquals("Training not found!", exception.getMessage());
        verify(trainingDAO, times(1)).isTrainingDBEmpty();
        verifyNoMoreInteractions(trainingDAO);
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

        verify(trainingDAO, times(1)).isTrainingDBEmpty();
        verify(trainingDAO, times(1)).findAll();
        verify(trainingMapper, times(1)).toResponses(trainings);
        verifyNoMoreInteractions(trainingDAO, trainingMapper);
    }
}