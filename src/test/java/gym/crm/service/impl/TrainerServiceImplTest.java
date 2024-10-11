package gym.crm.service.impl;

import gym.crm.dto.reponse.RegistrationResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.mapper.TrainerMapper;
import gym.crm.model.Trainer;
import gym.crm.model.TrainingType;
import gym.crm.repository.TrainerRepository;
import gym.crm.repository.TrainingTypeRepository;
import gym.crm.util.PasswordGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    void testUsernameExists_AddedSuffix() {
        TrainerRequest trainerRequest = new TrainerRequest("ali", "vali", 1L, true);
        Trainer trainer = new Trainer();
        trainer.setUsername("ali.vali");
        String pswd = "pswd";
        trainer.setPassword(pswd);

        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setName("GYM");

        try (MockedStatic<PasswordGenerator> mockPasswordGenerator = mockStatic(PasswordGenerator.class)) {
            mockPasswordGenerator.when(PasswordGenerator::generatePassword).thenReturn(pswd);

            when(passwordEncoder.encode(pswd)).thenReturn(anyString());
            when(trainerMapper.toTrainer(trainerRequest)).thenReturn(trainer);
            when(trainingTypeRepository.findById(1L)).thenReturn(Optional.of(trainingType));
            when(trainerRepository.existsTrainerByUsername("ali.vali")).thenReturn(true);

            trainerService.create(trainerRequest);

            assertEquals("ali.vali1", trainer.getUsername());

            verify(trainerMapper, times(1)).toTrainer(trainerRequest);
            verify(trainerRepository, times(1)).existsTrainerByUsername("ali.vali");
            verify(trainerRepository, times(1)).save(trainer);
            verifyNoMoreInteractions(trainerMapper, trainerRepository);
        }
    }

    @Test
    void testUsername_Success() {
        TrainerRequest trainerRequest = new TrainerRequest("ali", "vali", 1L, true);
        Trainer trainer = new Trainer();
        trainer.setUsername("ali.vali");
        String pswd = "pswd";
        trainer.setPassword(pswd);

        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setName("GYM");

        when(trainerMapper.toTrainer(trainerRequest)).thenReturn(trainer);
        try (MockedStatic<PasswordGenerator> mockPasswordGenerator = mockStatic(PasswordGenerator.class)) {
            mockPasswordGenerator.when(PasswordGenerator::generatePassword).thenReturn(pswd);
            when(trainingTypeRepository.findById(1L)).thenReturn(Optional.of(trainingType));
            when(trainerRepository.existsTrainerByUsername("ali.vali")).thenReturn(false);

            RegistrationResponse response = trainerService.create(trainerRequest);

            assertEquals(response.username(), trainer.getUsername());

            verify(trainerMapper, times(1)).toTrainer(trainerRequest);
            verify(trainingTypeRepository, times(1)).findById(1L);
            verify(trainerRepository, times(1)).existsTrainerByUsername("ali.vali");
            verify(trainerRepository, times(1)).save(trainer);
        }
    }

    @Test
    void updateFails() {
        TrainerRequest trainerRequest = new TrainerRequest("jakie", "chan", 1L, true);
        Trainer trainer = new Trainer();
        trainer.setUsername("jakie.chan");

        when(trainerRepository.findByUsername(trainer.getUsername())).thenReturn(Optional.empty());

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            trainerService.update(trainer.getUsername(), trainerRequest);
        });

        assertEquals("Trainer not found!", exception.getMessage());

        verify(trainerRepository, times(1)).findByUsername(trainer.getUsername());
        verify(trainerRepository, never()).save(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerRepository);
    }

    @Test
    void findByUsername_Fails() {
        when(trainerRepository.findByUsername("username")).thenReturn(Optional.empty());
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            trainerService.findByUsername("username");
        });

        assertEquals("Trainer not found!", exception.getMessage());
        verify(trainerRepository, times(1)).findByUsername("username");
        verifyNoMoreInteractions(trainerMapper, trainerRepository);
    }

    @Test
    void findByUsername_Success() {
        String username = "username";
        Trainer trainer = new Trainer();
        TrainerResponse trainerResponse = new TrainerResponse(1L, "b", "c", "f", true, null);

        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
        when(trainerMapper.toTrainerResponse(trainer)).thenReturn(trainerResponse);

        TrainerResponse response = trainerService.findByUsername(username);

        assertEquals(trainerResponse, response);

        verify(trainerRepository, times(1)).findByUsername(username);
        verify(trainerMapper, times(1)).toTrainerResponse(trainer);
        verifyNoMoreInteractions(trainerMapper, trainerRepository);
    }

    @Test
    void findAll_Fails() {
        when(trainerRepository.findAll()).thenReturn(Collections.emptyList());
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> trainerService.findAll());
        assertEquals("Trainers not found!",  exception.getMessage());
        verify(trainerRepository, times(1)).findAll();
        verifyNoMoreInteractions(trainerMapper, trainerRepository);
    }

    @Test
    void findAllSuccess() {
        Trainer trainer1 = new Trainer();
        Trainer trainer2 = new Trainer();

        TrainerResponse trainerResponse1 = new TrainerResponse(1L, "b", "c", "f", true, null);
        TrainerResponse trainerResponse2 = new TrainerResponse(1L, "b", "c", "f", true, null);

        List<Trainer> trainers = List.of(trainer1, trainer2);
        List<TrainerResponse> trainerResponses = List.of(trainerResponse1, trainerResponse2);

        when(trainerRepository.findAll()).thenReturn(trainers);
        when(trainerMapper.toTrainerResponses(trainers)).thenReturn(trainerResponses);

        List<TrainerResponse> response = trainerService.findAll();

        assertEquals(trainerResponses, response);

        verify(trainerRepository, times(1)).findAll();
        verify(trainerMapper, times(1)).toTrainerResponses(trainers);
        verifyNoMoreInteractions(trainerMapper, trainerRepository);
    }

    @Test
    public void testUpdatePassword_Success() {
        String username = "JohnDoe";
        String oldPassword = "oldPass";
        String newPassword = "newPass";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setPassword(oldPassword);

        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));

        trainerService.updatePassword(username, oldPassword, newPassword);

        verify(trainerRepository, times(1)).findByUsername(username);
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    public void testDeActivateUser_Success() {
        String username = "JohnDoe";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setIsActive(true);

        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));

        trainerService.deActivateUser(username);

        verify(trainerRepository, times(1)).findByUsername(username);
        verify(trainerRepository, times(1)).save(trainer);
    }

}