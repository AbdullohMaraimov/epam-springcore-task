package gym.crm.controller;

import gym.crm.controller.documentation.AuthControllerDocumentation;
import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.RegistrationResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.dto.request.UserLoginRequest;
import gym.crm.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocumentation {

    private final AuthService authService;

    @PostMapping("/register-trainee")
    public ApiResponse<RegistrationResponse> register(@RequestBody @Valid TraineeRequest dto) throws IOException {
        log.info("Registering trainee with the request : {}", dto);
        RegistrationResponse registrationResponse = authService.register(dto);
        return new ApiResponse<>(201, true, registrationResponse, "Saved successfully!");
    }

    @PostMapping("/register-trainer")
    public ApiResponse<RegistrationResponse> register(@RequestBody @Valid TrainerRequest dto) throws IOException {
        log.info("Registering trainer with the request : {}", dto);
        RegistrationResponse registrationResponse = authService.register(dto);
        return new ApiResponse<>(201, true,   registrationResponse, "Saved successfully!");
    }

    @GetMapping("/login")
    public ApiResponse<String> login(@Valid @RequestBody UserLoginRequest dto) {
        log.info("Logging in with username : {}", dto.username());
        String login = authService.login(dto);
        return new ApiResponse<>(200, true, login, "OK");
    }

}
