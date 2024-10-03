package gym.crm.service.impl;

import gym.crm.dto.reponse.RegistrationResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.reponse.TraineeResponse;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.mapper.TrainerMapper;
import gym.crm.model.Trainee;
import gym.crm.mapper.TraineeMapper;
import gym.crm.model.Trainer;
import gym.crm.repository.TraineeRepository;
import gym.crm.repository.TrainerRepository;
import gym.crm.repository.TrainingRepository;
import gym.crm.service.TraineeService;
import gym.crm.util.PasswordGenerator;
import gym.crm.util.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;

    @Override
    @Transactional
    public RegistrationResponse create(TraineeRequest trainee) {
        log.info("Creating new trainee with request: {}", trainee);
        Trainee newTrainee = traineeMapper.toTrainee(trainee);
        newTrainee.setPassword(PasswordGenerator.generatePassword());


        if(traineeRepository.existsTraineeByUsername(newTrainee.getUsername())) {
            newTrainee.setUsername(newTrainee.getUsername() + Utils.traineeIdx++);
            log.info("Username already exists, changed to {}", newTrainee.getUsername());
        }

        traineeRepository.save(newTrainee);
        log.info("Trainee saved : {}", newTrainee.getUsername());
        return new RegistrationResponse(newTrainee.getUsername(), newTrainee.getPassword());

    }

    @Override
    @Transactional
    public TraineeResponse update(String username, TraineeRequest traineeRequest) {
        log.info("Updating trainee with username: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("Trainee with id %s not found".formatted(username)));

        Trainee updatedTrainee = traineeMapper.toUpdatedTrainee(trainee, traineeRequest);
        TraineeResponse traineeResponse = traineeMapper.toTraineeResponse(updatedTrainee);
        traineeRepository.save(updatedTrainee);
        log.info("Trainee updated successfully: {}", updatedTrainee);
        return traineeResponse;
    }

    @Override
    @Transactional
    public void updatePassword(String username, String oldPassword, String newPassword) {
        log.info("Updating password for username {}", username);
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("Trainee with username %s not found".formatted(username)));

        if (!Objects.equals(trainee.getPassword(), oldPassword)) {
            throw new IllegalArgumentException("Update password operation failed");
        }
        trainee.setPassword(newPassword);
        traineeRepository.save(trainee);
    }

    @Override
    public void deActivateUser(String username) {
        log.info("Deactivating user with username: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("Trainee with username %s not found".formatted(username)));

        if (!trainee.getIsActive()) {
            throw new IllegalArgumentException("User already inactive");
        }
        trainee.setIsActive(false);
        traineeRepository.save(trainee);
    }

    @Override
    public void activateUser(String username) {
        log.info("Activating user with username: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("Trainee with username %s not found".formatted(username)));

        if (trainee.getIsActive()) {
            throw new IllegalArgumentException("User already active");
        }
        trainee.setIsActive(true);
        traineeRepository.save(trainee);
    }

    @Override
    @Transactional
    public void delete(String username) {
        log.info("Deleting trainee with username: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("Trainee with username %s not found".formatted(username)));

        for (Trainer trainer : trainee.getTrainers()) {
            trainer.getTrainees().remove(trainee);
            trainerRepository.save(trainer);
        }
        trainingRepository.deleteByTraineeUsername(username);
        traineeRepository.deleteByUsername(username);
        log.info("Trainee with username {} deleted successfully", username);
    }

    @Override
    public TraineeResponse findByUsername(String username) {
        log.info("Finding trainee with username: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("Trainee with username %s not found".formatted(username)));

        return traineeMapper.toTraineeResponse(trainee);
    }

    @Override
    public List<TraineeResponse> findAll() {
        log.info("Finding all trainees");
        List<Trainee> trainees = traineeRepository.findAll();
        if (trainees.isEmpty()) {
            throw new CustomNotFoundException("Trainees not found!");
        }
        return traineeMapper.toTraineeResponses(trainees);
    }

    @Override
    public List<TrainerResponse> findAllUnassignedTrainers(String username) {
        log.info("Finding all unassigned trainers for trainee with username: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("Trainee with username %s not found".formatted(username)));

        List<Trainer> trainers = trainee.getTrainers();
        List<Trainer> allTrainers = trainerRepository.findAll();
        allTrainers.removeAll(trainers);
        return trainerMapper.toTrainerResponses(allTrainers);
    }

    @Override
    @Transactional
    public void deleteAll() {
        log.info("Deleting all trainees");
        traineeRepository.deleteAll();
        log.info("All trainees deleted");
    }
}
