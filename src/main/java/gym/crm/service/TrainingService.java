package gym.crm.service;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainingRequest;

import java.util.List;

public interface TrainingService {

    ApiResponse<Void> create(TrainingRequest trainingRequest);

    ApiResponse<TrainingResponse> findById(String id);

    ApiResponse<List<TrainingResponse>> findAll();

}
