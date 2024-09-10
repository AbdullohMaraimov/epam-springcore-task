package gym.crm.service;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.dto.request.UserLoginRequest;

import java.io.IOException;

public interface AuthService {
    ApiResponse<Void> register(TraineeRequest registerDto) throws IOException;
    ApiResponse<Void> register(TrainerRequest registerDto) throws IOException;
    String login(UserLoginRequest loginDto);
}
