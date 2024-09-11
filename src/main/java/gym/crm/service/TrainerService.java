package gym.crm.service;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TrainerRequest;

import java.util.List;

public interface TrainerService {

    ApiResponse<Void> create(TrainerRequest trainer);

    ApiResponse<Void> update(String username, TrainerRequest trainer);

    ApiResponse<TrainerResponse> findByUsername(String username);

    ApiResponse<List<TrainerResponse>> findAll();

    ApiResponse<Void> updatePassword(String username, String oldPassword, String newPassword);

    ApiResponse<Void> deActivateUser(String username);

    ApiResponse<Void> activateUser(String username);
}
