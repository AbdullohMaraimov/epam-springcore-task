package gym.crm.controller.documentation;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.RegistrationResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.dto.request.UserLoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

public interface AuthControllerDocumentation {

    @Operation(summary = "Register a new trainee", description = "This endpoint registers a new trainee using the provided request data.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Saved successfully!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Username already exists, so changed it to newUsername",
                    content = @Content(mediaType = "application/json")),
    })
    @PostMapping("/register-trainee")
    ApiResponse<RegistrationResponse> register(@RequestBody @Valid TraineeRequest dto) throws Exception;


    @Operation(summary = "Register a new trainer", description = "This endpoint registers a new trainer using the provided request data.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Saved successfully!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Username already exists, so changed it to newUsername",
                    content = @Content(mediaType = "application/json")),
    })
    @PostMapping("/register-trainer")
    ApiResponse<RegistrationResponse> register(@RequestBody @Valid TrainerRequest dto) throws Exception;


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
    ApiResponse<String> login(@Valid @RequestBody UserLoginRequest dto);

}
