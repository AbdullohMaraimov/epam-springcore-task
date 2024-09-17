package gym.crm.service;

import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainingRequest;
import gym.crm.model.TrainingType;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {

    void create(TrainingRequest trainingRequest);

    TrainingResponse findById(Long id);

    List<TrainingResponse> findAll();

    List<TrainingResponse> findTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainingName, Long trainingTypeId);

    List<TrainingResponse> getTrainingsByTrainer(String username, LocalDate fromDate, LocalDate toDate, String traineeName);

    List<TrainingType> findAllTrainingTypes();
}
