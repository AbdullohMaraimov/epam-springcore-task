package gym.crm.service.impl;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainingRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.mapper.TrainingMapper;
import gym.crm.model.Training;
import gym.crm.repository.TraineeDAO;
import gym.crm.repository.TrainerDAO;
import gym.crm.repository.TrainingDAO;
import gym.crm.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
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
        log.debug("Creating training with request: {}", trainingRequest);
        if (trainerDAO.isUsernameExists(trainerId) && traineeDAO.isUsernameExists(traineeId)) {
            Training training = trainingMapper.toEntity(trainingRequest);
            trainingDAO.save(training);
            log.info("Training created successfully with ID: {}", training.getId());
            return new ApiResponse<>(204,true, null, "Training created successfully!");
        }
        log.error("Trainer or Trainee does not exist!");
        throw new CustomNotFoundException("Trainer or Trainee does not exist!");
    }

    @Override
    public ApiResponse<TrainingResponse> findById(String id) {
        log.debug("Finding training with ID: {}", id);
        if (trainingDAO.isTrainingExist(id)) {
            Training training = trainingDAO.findById(id);
            TrainingResponse response = trainingMapper.toResponse(training);
            log.info("Training found with ID: {}", id);
            return new ApiResponse<>(204,true, response, "Successfully found");
        } else {
            log.error("Training with ID " + id + " not found!");
            throw new CustomNotFoundException("Training not found!");
        }
    }

    @Override
    public ApiResponse<List<TrainingResponse>> findAll() {
        log.debug("Finding all trainings");
        if (trainingDAO.isTrainingDBEmpty()) {
            log.error("No trainings found!");
            throw new CustomNotFoundException("Training not found!");
        }
        List<Training> trainings = trainingDAO.findAll();
        List<TrainingResponse> trainingResponses = trainingMapper.toResponses(trainings);
        log.info("Found {} trainings", trainingResponses.size());
        return new ApiResponse<>(200,true, trainingResponses, "Success!");
    }
}
