package gym.crm.service;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainingRequest;
import gym.crm.model.TrainingType;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {

    ApiResponse<Void> create(TrainingRequest trainingRequest);

    ApiResponse<TrainingResponse> findById(Long id);

    ApiResponse<List<TrainingResponse>> findAll();

    ApiResponse<List<TrainingResponse>> findTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainingName, Long trainingTypeId);

    ApiResponse<List<TrainingResponse>> getTrainingsByTrainer(String username, LocalDate fromDate, LocalDate toDate, String traineeName);

    ApiResponse<List<TrainingType>> findAllTrainingTypes();
}
