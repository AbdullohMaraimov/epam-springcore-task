package gym_crm.service;

import gym_crm.dto.reponse.ApiResponse;
import gym_crm.dto.request.TraineeRequest;
import gym_crm.dto.reponse.TraineeResponse;
import gym_crm.model.Trainee;

import java.util.List;

public interface TraineeService {

    ApiResponse<Void> create(TraineeRequest trainee);

    ApiResponse<Void> update(String username, TraineeRequest trainee);

    ApiResponse<Void> delete(String username);

    ApiResponse<TraineeResponse> findByUsername(String username);

    ApiResponse<List<TraineeResponse>> findAll();

    ApiResponse<Void> deleteAll();
}
