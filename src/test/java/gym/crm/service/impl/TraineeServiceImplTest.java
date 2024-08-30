package gym.crm.service.impl;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TraineeResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.mapper.TraineeMapper;
import gym.crm.model.Trainee;
import gym.crm.repository.TraineeDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceImplTest {

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private TraineeDAO traineeDAO;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUsernameAlreadyExists() {
        TraineeRequest request = new TraineeRequest("ali", "valiev", LocalDate.now(), "A232", true);
        Trainee trainee = new Trainee();
        trainee.setUsername("ali.valiev");

        when(traineeMapper.toTrainee(request)).thenReturn(trainee);
        when(traineeDAO.isUsernameExists(trainee.getUsername())).thenReturn(true);

        ApiResponse<Void> response = traineeService.create(request);

        assertEquals("Username already exists, so changed it to " + trainee.getUsername(), response.message());

        verify(traineeMapper, times(1)).toTrainee(request);
        verify(traineeDAO, times(1)).isUsernameExists("ali.valiev");
        verify(traineeDAO, times(1)).save(trainee);
        verifyNoMoreInteractions(traineeMapper, traineeDAO);
    }

    @Test
    void testCreateSuccess() {
        TraineeRequest request = new TraineeRequest("ali", "valiev", LocalDate.now(), "A232", true);
        Trainee trainee = new Trainee();
        trainee.setUsername("username");

        when(traineeMapper.toTrainee(request)).thenReturn(trainee);
        when(traineeDAO.isUsernameExists(trainee.getUsername())).thenReturn(false);

        ApiResponse<Void> response = traineeService.create(request);

        assertEquals("Saved successfully!", response.message());

        verify(traineeMapper, times(1)).toTrainee(request);
        verify(traineeDAO, times(1)).isUsernameExists(trainee.getUsername());
        verify(traineeDAO, times(1)).save(trainee);
        verifyNoMoreInteractions(traineeMapper, traineeDAO);
    }

    @Test
    void updateUpdateSuccess() {
        TraineeRequest request = new TraineeRequest("ali", "valiev", LocalDate.now(), "A232", true);
        Trainee trainee = new Trainee();
        Trainee updatedTrainee = new Trainee();

        when(traineeDAO.isUsernameExists("username")).thenReturn(true);
        when(traineeDAO.findByUsername("username")).thenReturn(trainee);
        when(traineeMapper.toUpdatedTrainee(trainee, request)).thenReturn(updatedTrainee);

        ApiResponse<Void> response = traineeService.update("username", request);

        assertEquals("Successfully updated!", response.message());

        verify(traineeDAO, times(1)).isUsernameExists("username");
        verify(traineeDAO, times(1)).findByUsername("username");
        verify(traineeMapper, times(1)).toUpdatedTrainee(trainee, request);
        verify(traineeDAO, times(1)).update(updatedTrainee);
        verifyNoMoreInteractions(traineeMapper, traineeDAO);
    }

    @Test
    public void testUpdateNotFound() {
        TraineeRequest request = new TraineeRequest("ali", "valiev", LocalDate.now(), "A232", true);
        when(traineeDAO.isUsernameExists("username")).thenReturn(false);

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            traineeService.update("username", request);
        });

        assertEquals("Trainee with id username not found", exception.getMessage());

        verify(traineeDAO, times(1)).isUsernameExists("username");
        verifyNoMoreInteractions(traineeMapper, traineeDAO);
    }

    @Test
    public void testDeleteSuccess() {
        when(traineeDAO.isUsernameExists("username")).thenReturn(true);
        ApiResponse<Void> response = traineeService.delete("username");

        assertEquals("Deleted successfully!", response.message());

        verify(traineeDAO, times(1)).isUsernameExists("username");
        verify(traineeDAO, times(1)).delete("username");
        verifyNoMoreInteractions(traineeDAO);
    }

    @Test
    public void testDeleteNotFound() {
        when(traineeDAO.isUsernameExists("username")).thenReturn(false);
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            traineeService.delete("username");
        });

        assertEquals("No Trainee found!", exception.getMessage());

        verify(traineeDAO, times(1)).isUsernameExists("username");
        verifyNoMoreInteractions(traineeDAO);
    }

    @Test
    void findByUsernameSuccess() {
        Trainee trainee = new Trainee();
        TraineeResponse traineeResponse = new TraineeResponse("A", "B", "C", "D", LocalDate.now(), "UZB" ,true);

        when(traineeDAO.isUsernameExists("username")).thenReturn(true);
        when(traineeDAO.findByUsername("username")).thenReturn(trainee);
        when(traineeMapper.toTraineeResponse(trainee)).thenReturn(traineeResponse);

        ApiResponse<TraineeResponse> response = traineeService.findByUsername("username");

        assertEquals("Successfully found!", response.message());
        assertEquals(traineeResponse, response.data());

        verify(traineeDAO, times(1)).isUsernameExists("username");
        verify(traineeDAO, times(1)).findByUsername("username");
        verify(traineeMapper, times(1)).toTraineeResponse(trainee);
        verifyNoMoreInteractions(traineeMapper, traineeDAO);
    }

    @Test
    public void testFindByUsernameNotFound() {
        when(traineeDAO.isUsernameExists("username")).thenReturn(false);
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            traineeService.findByUsername("username");
        });

        assertEquals("Trainee not found!", exception.getMessage());
        verify(traineeDAO, times(1)).isUsernameExists("username");
        verifyNoMoreInteractions(traineeDAO);
    }

    @Test
    public void testFindAllSuccess() {
        List<Trainee> trainees = List.of(new Trainee());
        List<TraineeResponse> traineeResponses = List.of(new TraineeResponse("A", "B", "C", "D", LocalDate.now(), "UZB" ,true));

        when(traineeDAO.isTraineeDBEmpty()).thenReturn(false);
        when(traineeDAO.findAll()).thenReturn(trainees);
        when(traineeMapper.toTraineeResponses(trainees)).thenReturn(traineeResponses);

        ApiResponse<List<TraineeResponse>> response = traineeService.findAll();

        assertEquals("Success!", response.message());
        assertEquals(traineeResponses, response.data());

        verify(traineeDAO, times(1)).isTraineeDBEmpty();
        verify(traineeDAO, times(1)).findAll();
        verify(traineeMapper, times(1)).toTraineeResponses(trainees);
        verifyNoMoreInteractions(traineeMapper, traineeDAO);
    }

    @Test
    public void testFindAllNoTrainees() {
        when(traineeDAO.isTraineeDBEmpty()).thenReturn(true);
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            traineeService.findAll();
        });
        assertEquals("Trainee not found!", exception.getMessage());
        verify(traineeDAO, times(1)).isTraineeDBEmpty();
        verifyNoMoreInteractions(traineeDAO);
    }

    @Test
    public void testDeleteAll() {
        ApiResponse<Void> response = traineeService.deleteAll();
        assertEquals("All Trainees deleted!", response.message());

        verify(traineeDAO, times(1)).deleteAll();
        verifyNoMoreInteractions(traineeDAO);
    }
}