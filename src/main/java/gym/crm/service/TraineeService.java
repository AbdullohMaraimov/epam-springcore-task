package gym.crm.service;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.reponse.TraineeResponse;

import java.util.List;

public interface TraineeService {

    ApiResponse<Void> create(TraineeRequest trainee);

    ApiResponse<Void> update(String username, TraineeRequest trainee);

    ApiResponse<Void> delete(String username);

    ApiResponse<TraineeResponse> findByUsername(String username);

    ApiResponse<List<TraineeResponse>> findAll();

    ApiResponse<Void> deleteAll();
}
