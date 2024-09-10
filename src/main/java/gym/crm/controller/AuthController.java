package gym.crm.controller;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.dto.request.UserLoginRequest;
import gym.crm.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-trainee")
    public ApiResponse<Void> register(@RequestBody @Valid TraineeRequest dto) throws IOException {
        log.info("Registering trainee with the request : {}", dto);
        return authService.register(dto);
    }

    @PostMapping("/register-trainer")
    public ApiResponse<Void> register(@RequestBody @Valid TrainerRequest dto) throws IOException {
        log.info("Registering trainer with the request : {}", dto);
        return authService.register(dto);
    }

    @GetMapping("/login")
    public String login(@Valid @RequestBody UserLoginRequest dto) {
        log.info("Logging in with username : {}", dto.username());
        return authService.login(dto);
    }

}
