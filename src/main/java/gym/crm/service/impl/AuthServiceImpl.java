package gym.crm.service.impl;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.dto.request.UserLoginRequest;
import gym.crm.model.User;
import gym.crm.repository.UserDAO;
import gym.crm.service.AuthService;
import gym.crm.service.JwtService;
import gym.crm.service.TraineeService;
import gym.crm.service.TrainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final JwtService jwtService;
    private final UserDAO userDAO;

    @Override
    public ApiResponse<Void> register(TraineeRequest registerDto) throws IOException {
        return traineeService.create(registerDto);
    }

    @Override
    public ApiResponse<Void> register(TrainerRequest registerDto) throws IOException {
        return trainerService.create(registerDto);
    }

    @Override
    public String login(UserLoginRequest loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password()));
        User user = userDAO.findByUsername(loginDto.username());
        return jwtService.generateToken(user);
    }
}
