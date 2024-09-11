package gym.crm.service.impl;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainingRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.mapper.TrainingMapper;
import gym.crm.model.Trainee;
import gym.crm.model.Trainer;
import gym.crm.model.Training;
import gym.crm.model.TrainingType;
import gym.crm.repository.TraineeDAO;
import gym.crm.repository.TrainerDAO;
import gym.crm.repository.TrainingDAO;
import gym.crm.repository.TrainingTypeDAO;
import gym.crm.service.TrainingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDAO trainingDAO;
    private final TrainerDAO trainerDAO;
    private final TraineeDAO traineeDAO;
    private final TrainingMapper trainingMapper;
    private final TrainingTypeDAO trainingTypeDAO;

    @Override
    @Transactional
    public ApiResponse<Void> create(TrainingRequest trainingRequest) {
        Long traineeId = trainingRequest.traineeId();
        Long trainerId = trainingRequest.trainerId();

        log.debug("Creating training with request: {}", trainingRequest);

        Trainer trainer = trainerDAO.findById(trainerId);
        if (trainer == null) throw new CustomNotFoundException("Trainer not found with id: %d".formatted(trainerId));
        Trainee trainee = traineeDAO.findById(traineeId);
        if (trainee == null) throw new CustomNotFoundException("Trainee not found with id: %d".formatted(trainerId));

        Training training = trainingMapper.toEntity(trainingRequest);

        TrainingType trainingType = trainingTypeDAO.findById(trainingRequest.trainingTypeId());
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);
        trainingDAO.save(training);

        trainee.addTrainer(trainer);
        trainer.addTrainee(trainee);

        traineeDAO.update(trainee);
        trainerDAO.update(trainer);

        log.info("Training created successfully with ID: {}", training.getId());
        return new ApiResponse<>(200,true, null, "Training created successfully!");
    }

    @Override
    @Transactional
    public ApiResponse<TrainingResponse> findById(Long id) {
        log.debug("Finding training with ID: {}", id);
        Training training = trainingDAO.findById(id);
        if (training == null) {
            throw new CustomNotFoundException("Training not found with id: %d".formatted(id));
        }
        TrainingResponse response = trainingMapper.toResponse(training);
        log.info("Training found with ID: {}", id);
        return new ApiResponse<>(200,true, response, "Successfully found");
    }

    @Override
    @Transactional
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

    @Override
    public ApiResponse<List<TrainingResponse>> findTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainerName, Long trainingTypeId) {
        log.debug("Finding trainee trainings for username: {}", username);
        List<Training> trainings = trainingDAO.findAllByCriteria(username, fromDate, toDate, trainerName, trainingTypeId);
        List<TrainingResponse> responses = trainingMapper.toResponses(trainings);
        return new ApiResponse<>(200, responses, "Successfully found!", true);
    }

    @Override
    public ApiResponse<List<TrainingResponse>> getTrainingsByTrainer(String username, LocalDate fromDate, LocalDate toDate, String traineeName) {
        log.debug("Finding trainings by trainer: {}", username);
        List<Training> trainings = trainingDAO.findAllByTrainerAndCategory(username, fromDate, toDate, traineeName);
        List<TrainingResponse> responses = trainingMapper.toResponses(trainings);
        return new ApiResponse<>(200, responses, "Successfully found!", true);
    }
}
