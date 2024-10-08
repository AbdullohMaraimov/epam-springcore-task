package gym.crm.controller.documentation;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TraineeResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TraineeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface TraineeControllerDocumentation {


    @Operation(summary = "Update a trainee", description = "Update trainee details using their username")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainee updated successfully",
                    content = @Content(schema = @Schema(implementation = TraineeResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainee not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    ApiResponse<TraineeResponse> update(@PathVariable String username, @Valid @RequestBody TraineeRequest request);


    @Operation(summary = "Update trainee's password", description = "Change password of a trainee by verifying the old password")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Password update failed")
    })
    ApiResponse<Void> updatePassword(@RequestParam String username, @RequestParam String oldPassword, @RequestParam String newPassword);


    @Operation(summary = "Deactivate a trainee", description = "Deactivate a trainee by their username")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainee deactivated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Deactivation failed")
    })
    ApiResponse<Void> deActivateUser(@RequestParam String username);


    @Operation(summary = "Activate a trainee", description = "Activate a trainee by their username")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainee activated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Activation failed")
    })
    ApiResponse<Void> activateUser(@RequestParam String username);


    @Operation(summary = "Find a trainee by username", description = "Retrieve trainee details by their username")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainee found",
                    content = @Content(schema = @Schema(implementation = TraineeResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    ApiResponse<TraineeResponse> findByUsername(@PathVariable String username);


    @Operation(summary = "Find all trainees", description = "Retrieve a list of all trainees")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of all trainees",
                    content = @Content(schema = @Schema(implementation = TraineeResponse.class)))
    })
    ApiResponse<List<TraineeResponse>> findAll();


    @Operation(summary = "Get trainee's trainings", description = "Retrieve all trainings for a specific trainee")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of trainings retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TrainingResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    public ApiResponse<List<TrainingResponse>> getTraineeTrainings(@PathVariable String username,
                                                                   @RequestParam(required = false) LocalDate fromDate,
                                                                   @RequestParam(required = false) LocalDate toDate,
                                                                   @RequestParam(required = false) String trainerUsername,
                                                                   @RequestParam(required = false) Long trainingTypeId);


    @Operation(summary = "Get unassigned trainers", description = "Retrieve a list of trainers not assigned to the trainee")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Unassigned trainers found successfully",
                    content = @Content(schema = @Schema(implementation = TrainerResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    ApiResponse<List<TrainerResponse>> getAllUnassignedTrainers(@PathVariable String username);


    @Operation(summary = "Delete a trainee", description = "Delete a trainee by their username")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainee deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    ApiResponse<Void> deleteByUsername(@PathVariable String username);


    @Operation(summary = "Delete all trainees", description = "Delete all trainees from the system")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "All trainees deleted successfully")
    })
    ApiResponse<Void> deleteAll();

}
