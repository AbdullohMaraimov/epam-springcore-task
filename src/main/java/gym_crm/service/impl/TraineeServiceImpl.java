package gym_crm.service.impl;

import gym_crm.dto.reponse.ApiResponse;
import gym_crm.dto.request.TraineeRequest;
import gym_crm.dto.reponse.TraineeResponse;
import gym_crm.model.Trainee;
import gym_crm.mapper.TraineeMapper;
import gym_crm.repository.TraineeDAO;
import gym_crm.service.TraineeService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeMapper traineeMapper;
    private final TraineeDAO traineeDAO;

    @Override
    public ApiResponse<Void> create(TraineeRequest trainee) {
        Trainee newTrainee = traineeMapper.toTrainee(trainee);
        newTrainee.setUserId(UUID.randomUUID());
        if(traineeDAO.isUsernameExists(newTrainee.getUsername())) {
            newTrainee.setUsername(newTrainee.getUsername() + TraineeDAO.index++);
            traineeDAO.save(newTrainee);
            return new ApiResponse<>(true, null, "Username already exists, so changed it to " + newTrainee.getUsername());
        } else {
            traineeDAO.save(newTrainee);
            return new ApiResponse<>(true, null, "Saved successfully!");
        }
    }

    @Override
    public ApiResponse<Void> update(String username, TraineeRequest traineeRequest) {
        if (traineeDAO.isUsernameExists(username)) {
            Trainee trainee = traineeDAO.findByUsername(username);
            Trainee updatedTrainee = traineeMapper.toUpdatedTrainee(trainee, traineeRequest);
            traineeDAO.update(updatedTrainee);
            return new ApiResponse<>(true, null, "Successfully updated!");
        } else {
            return new ApiResponse<>(false, null, "No trainee find with the given username!");
        }
    }

    @Override
    public ApiResponse<Void> delete(String username) {
        if (traineeDAO.isUsernameExists(username)) {
            traineeDAO.delete(username);
            return new ApiResponse<>(true, null, "Deleted successfully!");
        } else {
            return new ApiResponse<>(false, null, "No Trainee found!");
        }
    }

    @Override
    public ApiResponse<TraineeResponse> findByUsername(String username) {
        if (traineeDAO.isUsernameExists(username)) {
            Trainee trainee = traineeDAO.findByUsername(username);
            TraineeResponse traineeResponse = traineeMapper.toTraineeResponse(trainee);
            return new ApiResponse<>(true, traineeResponse, "Successfully found!");
        } else {
            return new ApiResponse<>(false, null , "Trainee not found!");
        }
    }

    @Override
    public ApiResponse<List<TraineeResponse>> findAll() {
        if (traineeDAO.isTraineeDBEmpty()) {
            return new ApiResponse<>(false, null, "No Trainee found!");
        } else {
            List<TraineeResponse> traineeResponses = traineeMapper.toTraineeResponses(traineeDAO.findAll());
            return new ApiResponse<>(true, traineeResponses, "Success!");
        }
    }

    @Override
    public ApiResponse<Void> deleteAll() {
        traineeDAO.deleteAll();
        return new ApiResponse<>(true, null, "All Trainees deleted!");
    }
}
