package gym.crm.service.impl;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainingRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.mapper.TrainingMapper;
import gym.crm.model.Trainee;
import gym.crm.model.Trainer;
import gym.crm.model.Training;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        TrainingRequest trainingRequest = new TrainingRequest(1L, 1L, "", 1L,LocalDate.now(), Duration.ofDays(1L));
        Trainer trainer = new Trainer();
        trainer.setTrainees(new ArrayList<>());
        trainer.setId(1L);

        Trainee trainee = new Trainee();
        trainee.setTrainers(new ArrayList<>());
        trainee.setId(1L);

        Training training = new Training();

        when(trainerDAO.findById(1L)).thenReturn(trainer);
        when(traineeDAO.findById(1L)).thenReturn(trainee);
        when(trainingMapper.toEntity(trainingRequest)).thenReturn(training);

        ApiResponse<Void> response = trainingService.create(trainingRequest);

        assertTrue(response.success());
        assertEquals("Training created successfully!", response.message());

        verify(trainerDAO, times(1)).findById(1L);
        verify(traineeDAO, times(1)).findById(1L);
        verify(trainingMapper, times(1)).toEntity(trainingRequest);
        verify(trainingDAO, times(1)).save(training);
        verify(traineeDAO, times(1)).update(trainee);
        verify(trainerDAO, times(1)).update(trainer);
        verifyNoMoreInteractions(trainerDAO, traineeDAO, trainingMapper, trainingDAO);
    }

    @Test
    public void testCreateTraining_TrainerNotFound() {
        Long traineeId = 1L;
        Long trainerId = 2L;

        TrainingRequest trainingRequest = new TrainingRequest(1L, 2L, "", 1L,LocalDate.now(), Duration.ofDays(1L));

        when(trainerDAO.findById(trainerId)).thenReturn(null);

        CustomNotFoundException e = assertThrows(CustomNotFoundException.class,
                () -> trainingService.create(trainingRequest));

        assertEquals(e.getMessage(), "Trainer not found with id: %d".formatted(trainerId));

        verify(trainerDAO, times(1)).findById(trainerId);
        verify(trainingDAO, never()).save(any(Training.class));
    }

    @Test
    void findByIdFails() {
        when(trainingDAO.findById(1L)).thenReturn(null);
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            trainingService.findById(1L);
        });
        assertEquals("Training not found with id: %d".formatted(1L), exception.getMessage());

        verify(trainingDAO, times(1)).findById(1L);
        verifyNoMoreInteractions(trainingDAO);
    }

    @Test
    void findById_Success() {
        Long trainingId = 1L;
        Training training = new Training();
        TrainingResponse trainingResponse = new TrainingResponse(
                1L, 1L, 1L, "", "GYM", LocalDate.now(), Duration.ZERO
        );

        when(trainingDAO.findById(trainingId)).thenReturn(training);
        when(trainingMapper.toResponse(training)).thenReturn(trainingResponse);

        ApiResponse<TrainingResponse> response = trainingService.findById(trainingId);

        assertEquals("Successfully found", response.message());
        assertEquals(trainingResponse, response.data());

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
                new TrainingResponse(1L, 1L, 1L, "", "GYM", LocalDate.now(), Duration.ZERO),
                new TrainingResponse(1L, 1L, 1L, "", "GYM", LocalDate.now(), Duration.ZERO)
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

    @Test
    public void testFindTraineeTrainings_Success() {
        String username = "trainee1";
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);
        String trainerName = "trainer1";
        Long trainingTypeId = 1L;

        List<Training> mockTrainings = List.of(new Training(), new Training());
        TrainingResponse response1 = new TrainingResponse(1L, 1L, 1L, "", "GYM", LocalDate.now(), Duration.ZERO);
        TrainingResponse response2 = new TrainingResponse(1L, 1L, 1L, "", "GYM", LocalDate.now(), Duration.ZERO);

        List<TrainingResponse> mockResponses = List.of(response1, response2);

        when(trainingDAO.findAllByCriteria(username, fromDate, toDate, trainerName, trainingTypeId)).thenReturn(mockTrainings);
        when(trainingMapper.toResponses(mockTrainings)).thenReturn(mockResponses);

        ApiResponse<List<TrainingResponse>> response = trainingService.findTraineeTrainings(username, fromDate, toDate, trainerName, trainingTypeId);

        assertEquals(200, response.statusCode());
        assertTrue(response.success());
        assertEquals("Successfully found!", response.message());

        verify(trainingDAO, times(1)).findAllByCriteria(username, fromDate, toDate, trainerName, trainingTypeId);
    }

    @Test
    public void testGetTrainingsByTrainer_Success() {
        String username = "trainer1";
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);
        String traineeName = "trainee1";

        TrainingResponse response1 = new TrainingResponse(1L, 1L, 1L, "", "GYM", LocalDate.now(), Duration.ZERO);
        TrainingResponse response2 = new TrainingResponse(1L, 1L, 1L, "", "GYM", LocalDate.now(), Duration.ZERO);

        List<Training> mockTrainings = List.of(new Training(), new Training());
        List<TrainingResponse> mockResponses = List.of(response1, response2);

        when(trainingDAO.findAllByTrainerAndCategory(username, fromDate, toDate, traineeName)).thenReturn(mockTrainings);
        when(trainingMapper.toResponses(mockTrainings)).thenReturn(mockResponses);

        ApiResponse<List<TrainingResponse>> response = trainingService.getTrainingsByTrainer(username, fromDate, toDate, traineeName);

        assertEquals(true, response.success());
        assertTrue(response.success());
        assertEquals("Successfully found!", response.message());

        verify(trainingDAO, times(1)).findAllByTrainerAndCategory(username, fromDate, toDate, traineeName);
    }

}