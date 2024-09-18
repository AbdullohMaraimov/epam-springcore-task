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
import gym.crm.repository.TraineeRepository;
import gym.crm.repository.TrainerRepository;
import gym.crm.repository.TrainingRepository;
import gym.crm.repository.TrainingTypeRepository;
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

    private final TrainingRepository trainingRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingMapper trainingMapper;
    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    @Transactional
    public void create(TrainingRequest trainingRequest) {
        Long traineeId = trainingRequest.traineeId();
        Long trainerId = trainingRequest.trainerId();

        log.debug("Creating training with request: {}", trainingRequest);

        Trainer trainer = trainerRepository.findById(trainerId);
        if (trainer == null) throw new CustomNotFoundException("Trainer not found with id: %d".formatted(trainerId));
        Trainee trainee = traineeRepository.findById(traineeId);
        if (trainee == null) throw new CustomNotFoundException("Trainee not found with id: %d".formatted(trainerId));

        Training training = trainingMapper.toEntity(trainingRequest);

        TrainingType trainingType = trainingTypeRepository.findById(trainingRequest.trainingTypeId());
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);
        trainingRepository.save(training);

        trainee.addTrainer(trainer);
        trainer.addTrainee(trainee);

        traineeRepository.update(trainee);
        trainerRepository.update(trainer);

        log.info("Training created successfully with ID: {}", training.getId());
    }

    @Override
    @Transactional
    public TrainingResponse findById(Long id) {
        log.debug("Finding training with ID: {}", id);
        Training training = trainingRepository.findById(id);
        if (training == null) {
            throw new CustomNotFoundException("Training not found with id: %d".formatted(id));
        }
        TrainingResponse response = trainingMapper.toResponse(training);
        log.info("Training found with ID: {}", id);
        return response;
    }

    @Override
    @Transactional
    public List<TrainingResponse> findAll() {
        log.debug("Finding all trainings");
        if (trainingRepository.isTrainingDBEmpty()) {
            log.error("No trainings found!");
            throw new CustomNotFoundException("Training not found!");
        }
        List<Training> trainings = trainingRepository.findAll();
        List<TrainingResponse> trainingResponses = trainingMapper.toResponses(trainings);
        log.info("Found {} trainings", trainingResponses.size());
        return trainingResponses;
    }

    @Override
    public List<TrainingResponse> findTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainerName, Long trainingTypeId) {
        log.debug("Finding trainee trainings for username: {}", username);
        List<Training> trainings = trainingRepository.findAllByCriteria(username, fromDate, toDate, trainerName, trainingTypeId);
        return trainingMapper.toResponses(trainings);
    }

    @Override
    public List<TrainingResponse> getTrainingsByTrainer(String username, LocalDate fromDate, LocalDate toDate, String traineeName) {
        log.debug("Finding trainings by trainer: {}", username);
        List<Training> trainings = trainingRepository.findAllByTrainerAndCategory(username, fromDate, toDate, traineeName);
        return trainingMapper.toResponses(trainings);
    }

    @Override
    public List<TrainingType> findAllTrainingTypes() {
        return trainingTypeRepository.findAll();
    }
}
