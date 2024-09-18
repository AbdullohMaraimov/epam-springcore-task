package gym.crm.controller;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.service.TrainerService;
import gym.crm.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Operation(summary = "Find a trainer by username", description = "Fetch trainer details using username")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainer found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainer not found",
            content = @Content)
    @GetMapping("/{username}")
    public ApiResponse<TrainerResponse> findByUsername(@PathVariable String username) {
        log.info("Finding trainer with username {}", username);
        TrainerResponse trainerResponse = trainerService.findByUsername(username);
        return new ApiResponse<>(200,true, trainerResponse, "Successfully found!");
    }


    @Operation(summary = "Fetch all trainers", description = "Get a list of all trainers")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of trainers retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No trainers found",
            content = @Content)
    @GetMapping
    public ApiResponse<List<TrainerResponse>> findAll() {
        log.info("Finding all trainers");
        List<TrainerResponse> trainerResponses = trainerService.findAll();
        return new ApiResponse<>(200,true, trainerResponses, "Success!");
    }


    @Operation(summary = "Get trainer's trainings", description = "Fetch a list of trainings for a specific trainer between optional date range and filtered by trainee name")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of trainings retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingResponse.class)))
    @GetMapping("/{username}/trainings")
    public ApiResponse<List<TrainingResponse>> getTrainerTrainings(@PathVariable String username,
                                                                   @RequestParam(required = false) LocalDate fromDate,
                                                                   @RequestParam(required = false) LocalDate toDate,
                                                                   @RequestParam(required = false) String traineeName) {
        log.info("Getting trainings for trainer {} from {} to {}, trainee: {}",
                username, fromDate, toDate, traineeName);

        List<TrainingResponse> responses = trainingService.getTrainingsByTrainer(username, fromDate, toDate, traineeName);
        return new ApiResponse<>(200, responses, "Successfully found!", true);
    }


    @Operation(summary = "Update trainer", description = "Update an existing trainer's details by username")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainer updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainer not found")
    @PutMapping("/{username}")
    public ApiResponse<TrainerResponse> update(@PathVariable String username,@Valid @RequestBody TrainerRequest request) {
        log.info("Updating trainer with username {}: {}", username, request);
        TrainerResponse trainerResponse = trainerService.update(username, request);
        return new ApiResponse<>(200,true, trainerResponse, "Successfully updated!");
    }


    @Operation(summary = "Update trainer password", description = "Change the password of a trainer")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password updated successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Old password is incorrect")
    @PatchMapping("/update-password")
    public ApiResponse<Void> updatePassword(@RequestParam String username,
                                            @RequestParam String oldPassword,
                                            @RequestParam String newPassword) {
        log.info("Updating password for trainer {}", username);
        trainerService.updatePassword(username, oldPassword, newPassword);
        return new ApiResponse<>(200, "Password update successful", true);
    }


    @Operation(summary = "Deactivate a trainer", description = "Deactivate a trainer by username")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainer deactivated successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainer not found")
    @PatchMapping("/de-activate")
    public ApiResponse<Void> deActivateUser(@RequestParam String username) {
        log.info("Deactivating trainer {}", username);
        trainerService.deActivateUser(username);
        return new ApiResponse<>(200, "User deActivated successfully", true);
    }


    @Operation(summary = "Activate a trainer", description = "Activate a trainer by username")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainer activated successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainer not found")
    @PatchMapping("/activate")
    public ApiResponse<Void> activateUser(@RequestParam String username) {
        log.info("Activating trainer {}", username);
        trainerService.activateUser(username);
        return new ApiResponse<>(200, "User Activated successfully", true);
    }

}
