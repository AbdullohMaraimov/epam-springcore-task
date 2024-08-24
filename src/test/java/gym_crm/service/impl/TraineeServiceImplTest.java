package gym_crm.service.impl;

import gym_crm.dto.reponse.ApiResponse;
import gym_crm.dto.reponse.TraineeResponse;
import gym_crm.dto.request.TraineeRequest;
import gym_crm.mapper.TraineeMapper;
import gym_crm.model.Trainee;
import gym_crm.repository.TraineeDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        trainee.setUsername("existingUsername");

        when(traineeMapper.toTrainee(request)).thenReturn(trainee);
        when(traineeDAO.isUsernameExists(trainee.getUsername())).thenReturn(true);

        ApiResponse<Void> response = traineeService.create(request);

        verify(traineeDAO).save(trainee);
        assertEquals("Username already exists, so changed it to " + trainee.getUsername(), response.message());
    }

    @Test
    void testCreateSuccess() {
        TraineeRequest request = new TraineeRequest("ali", "valiev", LocalDate.now(), "A232", true);
        Trainee trainee = new Trainee();
        trainee.setUsername("username");

        when(traineeMapper.toTrainee(request)).thenReturn(trainee);
        when(traineeDAO.isUsernameExists(trainee.getUsername())).thenReturn(false);

        ApiResponse<Void> response = traineeService.create(request);

        verify(traineeDAO).save(trainee);
        assertEquals("Saved successfully!", response.message());
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

        verify(traineeDAO).update(updatedTrainee);
        assertEquals("Successfully updated!", response.message());
    }

    @Test
    public void testUpdateNotFound() {
        TraineeRequest request = new TraineeRequest("ali", "valiev", LocalDate.now(), "A232", true);
        when(traineeDAO.isUsernameExists("username")).thenReturn(false);
        ApiResponse<Void> response = traineeService.update("username", request);
        assertEquals("No trainee find with the given username!", response.message());
    }

    @Test
    public void testDeleteSuccess() {
        when(traineeDAO.isUsernameExists("username")).thenReturn(true);
        ApiResponse<Void> response = traineeService.delete("username");
        verify(traineeDAO).delete("username");
        assertEquals("Deleted successfully!", response.message());
    }

    @Test
    public void testDeleteNotFound() {
        when(traineeDAO.isUsernameExists("username")).thenReturn(false);
        ApiResponse<Void> response = traineeService.delete("username");
        assertEquals("No Trainee found!", response.message());
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
    }

    @Test
    public void testFindByUsernameNotFound() {
        when(traineeDAO.isUsernameExists("username")).thenReturn(false);
        ApiResponse<TraineeResponse> response = traineeService.findByUsername("username");
        assertEquals("Trainee not found!", response.message());
        assertNull(response.data());
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
    }

    @Test
    public void testFindAllNoTrainees() {
        when(traineeDAO.isTraineeDBEmpty()).thenReturn(true);
        ApiResponse<List<TraineeResponse>> response = traineeService.findAll();
        assertEquals("No Trainee found!", response.message());
        assertEquals(null, response.data());
    }

    @Test
    public void testDeleteAll() {
        ApiResponse<Void> response = traineeService.deleteAll();
        verify(traineeDAO).deleteAll();
        assertEquals("All Trainees deleted!", response.message());
    }
}