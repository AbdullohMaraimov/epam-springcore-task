package gym_crm.service;

import gym_crm.dto.reponse.ApiResponse;
import gym_crm.dto.reponse.TrainingResponse;
import gym_crm.dto.request.TrainingRequest;

import java.util.List;

public interface TrainingService {

    ApiResponse<Void> create(TrainingRequest trainingRequest);

    ApiResponse<TrainingResponse> findById(String id);

    ApiResponse<List<TrainingResponse>> findAll();

}
