package gym_crm.service.impl;

import gym_crm.dto.reponse.ApiResponse;
import gym_crm.dto.reponse.TrainingResponse;
import gym_crm.dto.request.TrainingRequest;
import gym_crm.mapper.TrainingMapper;
import gym_crm.model.Training;
import gym_crm.repository.TraineeDAO;
import gym_crm.repository.TrainerDAO;
import gym_crm.repository.TrainingDAO;
import gym_crm.service.TrainingService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDAO trainingDAO;
    private final TrainerDAO trainerDAO;
    private final TraineeDAO traineeDAO;
    private final TrainingMapper trainingMapper;

    @Override
    public ApiResponse<Void> create(TrainingRequest trainingRequest) {
        String traineeId = trainingRequest.traineeId();
        String trainerId = trainingRequest.trainerId();
        if (trainerDAO.isUsernameExists(trainerId) && traineeDAO.isUsernameExists(traineeId)) {
            Training training = trainingMapper.toEntity(trainingRequest);
            trainingDAO.save(training);
            return new ApiResponse<>(true, null, "Training created successfully!");
        }
        return new ApiResponse<>(false, null, "Trainer or Trainee does not exist!");
    }

    @Override
    public ApiResponse<TrainingResponse> findById(String id) {
        if (trainingDAO.isTrainingExist(id)) {
            Training training = trainingDAO.findById(id);
            TrainingResponse response = trainingMapper.toResponse(training);
            return new ApiResponse<>(true, response, "Successfully found");
        } else {
            return new ApiResponse<>(false, null, "Training not found!");
        }
    }

    @Override
    public ApiResponse<List<TrainingResponse>> findAll() {
        if (trainingDAO.isTrainingDBEmpty()) {
            return new ApiResponse<>(false, null, "No Training found!");
        }
        List<Training> trainings = trainingDAO.findAll();
        List<TrainingResponse> trainingResponses = trainingMapper.toResponses(trainings);
        return new ApiResponse<>(true, trainingResponses, "Success!");
    }
}
