package gym.crm.service.impl;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.reponse.TraineeResponse;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.model.Trainee;
import gym.crm.mapper.TraineeMapper;
import gym.crm.repository.TraineeDAO;
import gym.crm.service.TraineeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {


    private final TraineeMapper traineeMapper;
    private final TraineeDAO traineeDAO;

    @Override
    public ApiResponse<Void> create(TraineeRequest trainee) {
        log.debug("Creating new trainee with request: {}", trainee);
        Trainee newTrainee = traineeMapper.toTrainee(trainee);
        newTrainee.setUserId(UUID.randomUUID());
        if(traineeDAO.isUsernameExists(newTrainee.getUsername())) {
            newTrainee.setUsername(newTrainee.getUsername() + TraineeDAO.index++);
            traineeDAO.save(newTrainee);
            log.info("Username already exists, changed to {}", newTrainee.getUsername());
            return new ApiResponse<>(204, "Username already exists, so changed it to " + newTrainee.getUsername(), true);
        } else {
            traineeDAO.save(newTrainee);
            log.info("Trainee saved successfully: {}", newTrainee);
            return new ApiResponse<>(204, "Saved successfully!", true);
        }
    }

    @Override
    public ApiResponse<Void> update(String username, TraineeRequest traineeRequest) {
        log.debug("Updating trainee with username: {}", username);
        if (traineeDAO.isUsernameExists(username)) {
            Trainee trainee = traineeDAO.findByUsername(username);
            Trainee updatedTrainee = traineeMapper.toUpdatedTrainee(trainee, traineeRequest);
            traineeDAO.update(updatedTrainee);
            log.info("Trainee updated successfully: {}", updatedTrainee);
            return new ApiResponse<>(204 , "Successfully updated!", true);
        } else {
            throw new CustomNotFoundException("Trainee with id %s not found".formatted(username));
        }
    }

    @Override
    public ApiResponse<Void> delete(String username) {
        log.debug("Deleting trainee with username: {}", username);
        if (traineeDAO.isUsernameExists(username)) {
            traineeDAO.delete(username);
            return new ApiResponse<>(204, "Deleted successfully!", true);
        } else {
            throw new CustomNotFoundException("No Trainee found!");
        }
    }

    @Override
    public ApiResponse<TraineeResponse> findByUsername(String username) {
        log.debug("Finding trainee with username: {}", username);
        if (traineeDAO.isUsernameExists(username)) {
            Trainee trainee = traineeDAO.findByUsername(username);
            TraineeResponse traineeResponse = traineeMapper.toTraineeResponse(trainee);
            return new ApiResponse<>(200, true, traineeResponse, "Successfully found!");
        } else {
            throw new CustomNotFoundException("Trainee not found!");
        }
    }

    @Override
    public ApiResponse<List<TraineeResponse>> findAll() {
        log.debug("Finding all trainees");
        if (traineeDAO.isTraineeDBEmpty()) {
            throw new CustomNotFoundException("Trainee not found!");
        } else {
            List<TraineeResponse> traineeResponses = traineeMapper.toTraineeResponses(traineeDAO.findAll());
            return new ApiResponse<>(200,true, traineeResponses, "Success!");
        }
    }

    @Override
    public ApiResponse<Void> deleteAll() {
        log.debug("Deleting all trainees");
        traineeDAO.deleteAll();
        log.info("All trainees deleted");
        return new ApiResponse<>( 204,true, null, "All Trainees deleted!");
    }
}
