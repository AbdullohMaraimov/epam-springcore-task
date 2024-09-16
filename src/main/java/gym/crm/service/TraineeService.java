package gym.crm.service;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.RegistrationResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.reponse.TraineeResponse;

import java.util.List;

public interface TraineeService {

    ApiResponse<RegistrationResponse> create(TraineeRequest trainee);

    ApiResponse<TraineeResponse> update(String username, TraineeRequest trainee);

    ApiResponse<Void> delete(String username);

    ApiResponse<TraineeResponse> findByUsername(String username);

    ApiResponse<List<TraineeResponse>> findAll();

    ApiResponse<Void> deleteAll();

    ApiResponse<Void> updatePassword(String username, String oldPassword, String newPassword);

    ApiResponse<Void> deActivateUser(String username);

    ApiResponse<Void> activateUser(String username);

    ApiResponse<List<TrainerResponse>> findAllUnassignedTrainers(String username);
}
