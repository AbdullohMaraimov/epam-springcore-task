package gym.crm.controller;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TraineeResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.service.TraineeService;
import gym.crm.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @Operation(summary = "Update a trainee", description = "Update trainee details using their username")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainee updated successfully",
                    content = @Content(schema = @Schema(implementation = TraineeResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainee not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PatchMapping("/{username}")
    public ApiResponse<TraineeResponse> update(@PathVariable String username, @Valid @RequestBody TraineeRequest request) {
        log.info("Updating trainee with username {}: {}", username, request);
        return traineeService.update(username, request);
    }


    @Operation(summary = "Update trainee's password", description = "Change password of a trainee by verifying the old password")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Password update failed")
    })
    @PatchMapping("/update-password")
    public ApiResponse<Void> updatePassword(@RequestParam String username,
                                            @RequestParam String oldPassword,
                                            @RequestParam String newPassword) {
        log.info("Updating password for username {}", username);
        return traineeService.updatePassword(username, oldPassword, newPassword);
    }


    @Operation(summary = "Deactivate a trainee", description = "Deactivate a trainee by their username")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainee deactivated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Deactivation failed")
    })
    @PatchMapping("/de-activate")
    public ApiResponse<Void> deActivateUser(@RequestParam String username) {
        log.info("Deactivating user with username {}", username);
        return traineeService.deActivateUser(username);
    }


    @Operation(summary = "Activate a trainee", description = "Activate a trainee by their username")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainee activated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Activation failed")
    })
    @PatchMapping("/activate")
    public ApiResponse<Void> activateUser(@RequestParam String username) {
        log.info("Activating user with username {}", username);
        return traineeService.activateUser(username);
    }


    @Operation(summary = "Find a trainee by username", description = "Retrieve trainee details by their username")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainee found",
                    content = @Content(schema = @Schema(implementation = TraineeResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @GetMapping("/{username}")
    public ApiResponse<TraineeResponse> findByUsername(@PathVariable String username) {
        log.info("Finding trainee with username {}", username);
        return traineeService.findByUsername(username);
    }


    @Operation(summary = "Find all trainees", description = "Retrieve a list of all trainees")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of all trainees",
                    content = @Content(schema = @Schema(implementation = TraineeResponse.class)))
    })
    @GetMapping
    public ApiResponse<List<TraineeResponse>> findAll() {
        log.info("Finding all trainees");
        return traineeService.findAll();
    }


    @Operation(summary = "Get trainee's trainings", description = "Retrieve all trainings for a specific trainee")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of trainings retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TrainingResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @GetMapping("/{username}/trainings")
    public ApiResponse<List<TrainingResponse>> getTraineeTrainings(@PathVariable String username,
                                                                   @RequestParam(required = false) LocalDate fromDate,
                                                                   @RequestParam(required = false) LocalDate toDate,
                                                                   @RequestParam(required = false) String trainerUsername,
                                                                   @RequestParam(required = false) Long trainingTypeId) {
        log.info("Finding trainings for trainee {} from {} to {} with name {} and type {}", username, fromDate, toDate, trainerUsername, trainingTypeId);
        return trainingService.findTraineeTrainings(username, fromDate, toDate, trainerUsername, trainingTypeId);
    }


    @Operation(summary = "Get unassigned trainers", description = "Retrieve a list of trainers not assigned to the trainee")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Unassigned trainers found successfully",
                    content = @Content(schema = @Schema(implementation = TrainerResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @GetMapping("/{username}/unassigned-trainers")
    public ApiResponse<List<TrainerResponse>> getAllUnassignedTrainers(@PathVariable String username) {
        log.info("Finding all unassigned trainers for trainee {}", username);
        return traineeService.findAllUnassignedTrainers(username);
    }


    @Operation(summary = "Delete a trainee", description = "Delete a trainee by their username")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainee deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @DeleteMapping("/{username}")
    public ApiResponse<Void> deleteByUsername(@PathVariable String username) {
        log.info("Deleting trainee with username {}", username);
        return traineeService.delete(username);
    }


    @Operation(summary = "Delete all trainees", description = "Delete all trainees from the system")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "All trainees deleted successfully")
    })
    @DeleteMapping
    public ApiResponse<Void> deleteAll() {
        log.info("Deleting all trainees");
        return traineeService.deleteAll();
    }

}
