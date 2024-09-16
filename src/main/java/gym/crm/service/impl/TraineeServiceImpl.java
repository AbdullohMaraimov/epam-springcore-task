package gym.crm.service.impl;

import gym.crm.dto.reponse.ApiResponse;
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
    public ApiResponse<RegistrationResponse> create(TraineeRequest trainee) {
        log.info("Creating new trainee with request: {}", trainee);
        Trainee newTrainee = traineeMapper.toTrainee(trainee);
        newTrainee.setPassword(PasswordGenerator.generatePassword());
        if(traineeRepository.isUsernameExists(newTrainee.getUsername())) {
            newTrainee.setUsername(newTrainee.getUsername() + TraineeRepository.index++);
            traineeRepository.save(newTrainee);
            log.info("Username already exists, changed to {}", newTrainee.getUsername());
            RegistrationResponse registrationResponse = new RegistrationResponse(newTrainee.getUsername(), newTrainee.getPassword());
            return new ApiResponse<>(204, true, registrationResponse, "Username already exists, so changed it to %s".formatted(newTrainee.getUsername()));
        } else {
            RegistrationResponse registrationResponse = new RegistrationResponse(newTrainee.getUsername(), newTrainee.getPassword());
            traineeRepository.save(newTrainee);
            log.info("Trainee saved successfully: {}", newTrainee);
            return new ApiResponse<>(204, true, registrationResponse, "Saved successfully!");
        }
    }

    @Override
    @Transactional
    public ApiResponse<TraineeResponse> update(String username, TraineeRequest traineeRequest) {
        log.info("Updating trainee with username: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username);
        if (trainee != null) {
            Trainee updatedTrainee = traineeMapper.toUpdatedTrainee(trainee, traineeRequest);
            TraineeResponse traineeResponse = traineeMapper.toTraineeResponse(updatedTrainee);
            traineeRepository.update(updatedTrainee);
            log.info("Trainee updated successfully: {}", updatedTrainee);
            return new ApiResponse<>(204 , true,   traineeResponse, "Successfully updated!");
        } else {
            throw new CustomNotFoundException("Trainee with id %s not found".formatted(username));
        }
    }

    @Override
    @Transactional
    public ApiResponse<Void> updatePassword(String username, String oldPassword, String newPassword) {
        log.info("Updating password for username {}", username);
        Trainee trainee = traineeRepository.findByUsername(username);
        if (trainee == null || !Objects.equals(trainee.getPassword(), oldPassword)) {
            return new ApiResponse<>(400, "Update password operation failed", false);
        }
        trainee.setPassword(newPassword);
        traineeRepository.save(trainee);
        return new ApiResponse<>(200, "Password successfully updated!", true);
    }

    @Override
    public ApiResponse<Void> deActivateUser(String username) {
        log.info("Deactivating user with username: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username);
        if (trainee == null) {
            return new ApiResponse<>(400, "DeActivate operation failed", false);
        }
        if (!trainee.getIsActive()) {
            return new ApiResponse<>(400, "User already inactive", false);
        }
        trainee.setIsActive(false);
        traineeRepository.save(trainee);
        return new ApiResponse<>(200, "User deActivated successfully", true);
    }

    @Override
    public ApiResponse<Void> activateUser(String username) {
        log.info("Activating user with username: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username);
        if (trainee == null) {
            return new ApiResponse<>(400, "Activate operation failed", false);
        }
        if (trainee.getIsActive()) {
            return new ApiResponse<>(400, "User already active", false);
        }
        trainee.setIsActive(true);
        traineeRepository.save(trainee);
        return new ApiResponse<>(200, "User deActivated successfully", true);
    }

    @Override
    @Transactional
    public ApiResponse<Void> delete(String username) {
        log.info("Deleting trainee with username: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username);
        if (trainee != null) {
            for (Trainer trainer : trainee.getTrainers()) {
                trainer.getTrainees().remove(trainee);
                trainerRepository.update(trainer);
            }
            trainingRepository.deleteTrainingByTraineeUsername(username);
            traineeRepository.deleteTraineeByUsername(username);
            log.info("Trainee with username {} deleted successfully", username);
            return new ApiResponse<>(200, "Deleted successfully!", true);
        } else {
            throw new CustomNotFoundException("Trainee with username %s not found".formatted(username));
        }
    }

    @Override
    public ApiResponse<TraineeResponse> findByUsername(String username) {
        log.info("Finding trainee with username: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username);
        if (trainee != null) {
            TraineeResponse traineeResponse = traineeMapper.toTraineeResponse(trainee);
            return new ApiResponse<>(200, true, traineeResponse, "Successfully found!");
        } else {
            throw new CustomNotFoundException("Trainee with username %s not found".formatted(username));
        }
    }

    @Override
    public ApiResponse<List<TraineeResponse>> findAll() {
        log.info("Finding all trainees");
        List<Trainee> trainees = traineeRepository.findAll();
        if (trainees.isEmpty()) {
            throw new CustomNotFoundException("Trainees not found!");
        }
        List<TraineeResponse> traineeResponses = traineeMapper.toTraineeResponses(trainees);
        return new ApiResponse<>(200,true, traineeResponses, "Success!");
    }

    @Override
    public ApiResponse<List<TrainerResponse>> findAllUnassignedTrainers(String username) {
        log.info("Finding all unassigned trainers for trainee with username: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username);
        if (trainee == null) {
            throw new CustomNotFoundException("Trainer with username %s not found".formatted(username));
        }
        List<Trainer> trainers = trainee.getTrainers();
        List<Trainer> allTrainers = trainerRepository.findAll();
        allTrainers.removeAll(trainers);
        List<TrainerResponse> trainerResponses = trainerMapper.toTrainerResponses(allTrainers);
        return new ApiResponse<>(200, trainerResponses, "Successfully retrieved", true);
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteAll() {
        log.info("Deleting all trainees");
        traineeRepository.deleteAll();
        log.info("All trainees deleted");
        return new ApiResponse<>( 204,true, null, "All Trainees deleted!");
    }
}
