package gym.crm.service.impl;

import gym.crm.dto.reponse.RegistrationResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.dto.request.UserLoginRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.model.User;
import gym.crm.repository.UserRepository;
import gym.crm.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    @Override
    public RegistrationResponse register(TraineeRequest registerDto) {
        return traineeService.create(registerDto);
    }

    @Override
    public RegistrationResponse register(TrainerRequest registerDto) {
        return trainerService.create(registerDto);
    }

    @Override
    public String login(UserLoginRequest loginDto) {
        if (loginAttemptService.isBlocked(loginDto.username())) {
            throw new LockedException("User is locked for a while due to many failed attempts, please try again later");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password()));

            loginAttemptService.loginSucceeded(loginDto.username());

            User user = userRepository.findByUsername(loginDto.username())
                    .orElseThrow(() -> new CustomNotFoundException("User with username : %s not found".formatted(loginDto.username())));
            return jwtService.generateToken(user);
        } catch (AuthenticationException e) {
            loginAttemptService.loginFailed(loginDto.username());
            throw new BadCredentialsException("Wrong username or password");
        }
    }
}
