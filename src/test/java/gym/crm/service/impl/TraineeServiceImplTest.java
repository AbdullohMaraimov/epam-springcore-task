package gym.crm.service.impl;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.RegistrationResponse;
import gym.crm.dto.reponse.TraineeResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.mapper.TraineeMapper;
import gym.crm.model.Trainee;
import gym.crm.model.Trainer;
import gym.crm.repository.TraineeRepository;
import gym.crm.repository.TrainerRepository;
import gym.crm.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceImplTest {

    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_WhenUsernameExists_ShouldChangeUsername() {
        TraineeRequest request = new TraineeRequest("Jim", "Rohn", LocalDate.now(), "USA", true);
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername("Jim.Rohn");

        when(traineeMapper.toTrainee(request)).thenReturn(trainee);
        when(traineeRepository.isUsernameExists(trainee.getUsername())).thenReturn(true);

        ApiResponse<RegistrationResponse> response = traineeService.create(request);

        assertEquals("Username already exists, so changed it to " + trainee.getUsername(), response.message());

        verify(traineeMapper, times(1)).toTrainee(request);
        verify(traineeRepository, times(1)).isUsernameExists("Jim.Rohn");
        verify(traineeRepository, times(1)).save(trainee);
        verifyNoMoreInteractions(traineeMapper, traineeRepository);
    }

    @Test
    void testCreate_Success() {
        TraineeRequest request = new TraineeRequest("Jim", "Rohn", LocalDate.now(), "USA", true);
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername("Jim.Rohn");

        when(traineeMapper.toTrainee(request)).thenReturn(trainee);
        when(traineeRepository.isUsernameExists(trainee.getUsername())).thenReturn(false);

        ApiResponse<RegistrationResponse> response = traineeService.create(request);

        assertEquals("Saved successfully!", response.message());

        verify(traineeMapper, times(1)).toTrainee(request);
        verify(traineeRepository, times(1)).isUsernameExists(trainee.getUsername());
        verify(traineeRepository, times(1)).save(trainee);
        verifyNoMoreInteractions(traineeMapper, traineeRepository);
    }

    @Test
    void updateUpdateSuccess() {
        TraineeRequest request = new TraineeRequest("Jimmy", "Rohn", LocalDate.now(), "USA", true);
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        Trainee updatedTrainee = new Trainee("Jimmy", "Rohn", true, LocalDate.now(), "USA");
        TraineeResponse traineeResponse = null;

        when(traineeRepository.findByUsername("Jim.Rohn")).thenReturn(trainee);
        when(traineeMapper.toUpdatedTrainee(trainee, request)).thenReturn(updatedTrainee);
        when(traineeMapper.toTraineeResponse(updatedTrainee)).thenReturn(traineeResponse);

        ApiResponse<TraineeResponse> response = traineeService.update("Jim.Rohn", request);

        assertEquals("Successfully updated!", response.message());

        verify(traineeRepository, times(1)).findByUsername("Jim.Rohn");
        verify(traineeMapper, times(1)).toUpdatedTrainee(trainee, request);
        verify(traineeRepository, times(1)).update(updatedTrainee);
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testUpdateNotFound() {
        String username = "Jim.Rohn";
        TraineeRequest request = new TraineeRequest("Jimmy", "Rohn", LocalDate.now(), "USA", true);

        when(traineeRepository.findByUsername(username)).thenReturn(null);

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            traineeService.update(username, request);
        });

        assertEquals("Trainee with id %s not found".formatted(username), exception.getMessage());

        verify(traineeRepository, never()).update(any(Trainee.class));
        verify(traineeMapper, never()).toUpdatedTrainee(any(), any());
    }

    @Test
    public void testDelete_Success() {
        String username = "Jim.Rohn";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);
        trainee.setTrainers(new ArrayList<>());

        Trainer trainer = new Trainer(null, null, new ArrayList<>(), new ArrayList<>());
        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);

        when(traineeRepository.findByUsername(username)).thenReturn(trainee);

        ApiResponse<Void> response = traineeService.delete(username);

        assertTrue(response.success());
        assertEquals("Deleted successfully!", response.message());

        verify(traineeRepository, times(1)).deleteTraineeByUsername(username);
        verify(trainingRepository, times(1)).deleteTrainingByTraineeUsername(username);
        verify(trainerRepository, times(1)).update(trainer);
    }

    @Test
    public void testDelete_WhenUserNotFound_Fails() {
        String username = "Jim.Rohn";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);

        when(traineeRepository.findByUsername(username)).thenReturn(null);
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            traineeService.delete(username);
        });

        assertEquals("Trainee with username %s not found".formatted(username), exception.getMessage());

        verify(traineeRepository, times(1)).findByUsername(username);
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    void findByUsername_Success() {
        String username = "Jim.Rohn";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);

        TraineeResponse traineeResponse = new TraineeResponse(null, "Jim", "Rohn", LocalDate.now(), "USA", true, null);

        when(traineeRepository.findByUsername(username)).thenReturn(trainee);
        when(traineeMapper.toTraineeResponse(trainee)).thenReturn(traineeResponse);

        ApiResponse<TraineeResponse> response = traineeService.findByUsername(username);

        assertEquals("Successfully found!", response.message());
        assertEquals(traineeResponse, response.data());

        verify(traineeRepository, times(1)).findByUsername(username);
        verify(traineeMapper, times(1)).toTraineeResponse(trainee);
        verifyNoMoreInteractions(traineeMapper, traineeRepository);
    }

    @Test
    public void testFindByUsername_NotFound() {
        when(traineeRepository.findByUsername("username")).thenReturn(null);
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            traineeService.findByUsername("username");
        });

        assertEquals("Trainee with username %s not found".formatted("username"), exception.getMessage());
        verify(traineeRepository).findByUsername("username");
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testFindAllSuccess() {
        List<Trainee> trainees = List.of(new Trainee());
        List<TraineeResponse> traineeResponses = List.of(new TraineeResponse(null, "Jim", "Rohn", LocalDate.now(), "USA", true,null));

        when(traineeRepository.findAll()).thenReturn(trainees);
        when(traineeMapper.toTraineeResponses(trainees)).thenReturn(traineeResponses);

        ApiResponse<List<TraineeResponse>> response = traineeService.findAll();

        assertEquals("Success!", response.message());
        assertEquals(traineeResponses, response.data());

        verify(traineeRepository, times(1)).findAll();
        verify(traineeMapper, times(1)).toTraineeResponses(trainees);
        verifyNoMoreInteractions(traineeMapper, traineeRepository);
    }

    @Test
    public void testFindAll_NoTrainees() {
        when(traineeRepository.findAll()).thenReturn(Collections.emptyList());
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            traineeService.findAll();
        });
        assertEquals("Trainees not found!", exception.getMessage());
        verify(traineeRepository, times(1)).findAll();
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testDeleteAll_Success() {
        ApiResponse<Void> response = traineeService.deleteAll();
        assertEquals("All Trainees deleted!", response.message());

        verify(traineeRepository, times(1)).deleteAll();
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testUpdatePassword_Success() {
        String username = "Jim.Rohn";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);
        trainee.setPassword(oldPassword);

        when(traineeRepository.findByUsername(username)).thenReturn(trainee);

        ApiResponse<Void> response = traineeService.updatePassword(username, oldPassword, newPassword);

        assertTrue(response.success());
        assertEquals("Password successfully updated!", response.message());

        verify(traineeRepository, times(1)).findByUsername(username);
        verify(traineeRepository, times(1)).save(trainee);
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testUpdatePassword_IncorrectOldPassword() {
        String username = "Jim.Rohn";
        String oldPassword = "wrongPassword";
        String newPassword = "newPassword";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);
        trainee.setPassword("correctOldPassword");

        when(traineeRepository.findByUsername(username)).thenReturn(trainee);

        ApiResponse<Void> response = traineeService.updatePassword(username, oldPassword, newPassword);

        assertFalse(response.success());
        assertEquals("Update password operation failed", response.message());

        verify(traineeRepository, times(1)).findByUsername(username);
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testDeActivateUser_Success() {
        String username = "Jim.Rohn";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);
        trainee.setIsActive(true);

        when(traineeRepository.findByUsername(username)).thenReturn(trainee);

        ApiResponse<Void> response = traineeService.deActivateUser(username);

        assertTrue(response.success());
        assertEquals("User deActivated successfully", response.message());

        verify(traineeRepository, times(1)).findByUsername(username);
        verify(traineeRepository, times(1)).save(trainee);
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testDeActivateUser_AlreadyInactive() {
        String username = "Jim.Rohn";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);
        trainee.setIsActive(false);

        when(traineeRepository.findByUsername(username)).thenReturn(trainee);

        ApiResponse<Void> response = traineeService.deActivateUser(username);

        assertFalse(response.success());
        assertEquals("User already inactive", response.message());

        verify(traineeRepository, times(1)).findByUsername(username);
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testActivateUser_Success() {
        String username = "Jim.Rohn";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);
        trainee.setIsActive(false);

        when(traineeRepository.findByUsername(username)).thenReturn(trainee);

        ApiResponse<Void> response = traineeService.activateUser(username);

        assertTrue(response.success());
        assertEquals("User deActivated successfully", response.message());

        verify(traineeRepository, times(1)).findByUsername(username);
        verify(traineeRepository, times(1)).save(trainee);
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testActivateUser_AlreadyActive() {
        String username = "Jim.Rohn";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);
        trainee.setIsActive(true);

        when(traineeRepository.findByUsername(username)).thenReturn(trainee);

        ApiResponse<Void> response = traineeService.activateUser(username);

        assertFalse(response.success());
        assertEquals("User already active", response.message());

        verify(traineeRepository, times(1)).findByUsername(username);
        verifyNoMoreInteractions(traineeRepository);
    }

}