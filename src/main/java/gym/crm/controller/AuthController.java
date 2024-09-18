package gym.crm.controller;

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
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new trainee", description = "This endpoint registers a new trainee using the provided request data.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Saved successfully!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Username already exists, so changed it to newUsername",
                    content = @Content(mediaType = "application/json")),
    })
    @PostMapping("/register-trainee")
    public ApiResponse<RegistrationResponse> register(@RequestBody @Valid TraineeRequest dto) throws IOException {
        log.info("Registering trainee with the request : {}", dto);
        RegistrationResponse registrationResponse = authService.register(dto);
        return new ApiResponse<>(201, true, registrationResponse, "Saved successfully!");
    }

    @Operation(summary = "Register a new trainer", description = "This endpoint registers a new trainer using the provided request data.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Saved successfully!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Username already exists, so changed it to newUsername",
                    content = @Content(mediaType = "application/json")),
    })
    @PostMapping("/register-trainer")
    public ApiResponse<RegistrationResponse> register(@RequestBody @Valid TrainerRequest dto) throws IOException {
        log.info("Registering trainer with the request : {}", dto);
        RegistrationResponse registrationResponse = authService.register(dto);
        return new ApiResponse<>(201, true,   registrationResponse, "Saved successfully!");
    }

    @Operation(summary = "Login", description = "This endpoint allows users to login with their username and password. A JWT token is returned upon successful authentication.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful, JWT token returned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - invalid credentials",
                    content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/login")
    public ApiResponse<String> login(@Valid @RequestBody UserLoginRequest dto) {
        log.info("Logging in with username : {}", dto.username());
        String login = authService.login(dto);
        return new ApiResponse<>(200, true, login, "OK");
    }

}
