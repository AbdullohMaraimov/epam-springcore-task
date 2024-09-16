package gym.crm.service;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.RegistrationResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.dto.request.UserLoginRequest;

import java.io.IOException;

public interface AuthService {
    ApiResponse<RegistrationResponse> register(TraineeRequest registerDto) throws IOException;
    ApiResponse<RegistrationResponse> register(TrainerRequest registerDto) throws IOException;
    ApiResponse<String> login(UserLoginRequest loginDto);
}
