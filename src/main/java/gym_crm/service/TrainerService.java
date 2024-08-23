package gym_crm.service;

import gym_crm.dto.reponse.ApiResponse;
import gym_crm.dto.reponse.TrainerResponse;
import gym_crm.dto.request.TrainerRequest;

import java.util.List;

public interface TrainerService {

    ApiResponse<Void> create(TrainerRequest trainer);

    ApiResponse<Void> update(String username, TrainerRequest trainer);

    ApiResponse<TrainerResponse> findByUsername(String username);

    ApiResponse<List<TrainerResponse>> findAll();

}
