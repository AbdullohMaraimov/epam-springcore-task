package gym.crm.controller.documentation;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainerRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface TrainerControllerDocumentation {

    @Operation(summary = "Find a trainer by username", description = "Fetch trainer details using username")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainer found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainer not found",
            content = @Content)
    ApiResponse<TrainerResponse> findByUsername(@PathVariable String username);


    @Operation(summary = "Fetch all trainers", description = "Get a list of all trainers")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of trainers retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No trainers found",
            content = @Content)
    ApiResponse<List<TrainerResponse>> findAll();


    @Operation(summary = "Get trainer's trainings", description = "Fetch a list of trainings for a specific trainer between optional date range and filtered by trainee name")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of trainings retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingResponse.class)))
    ApiResponse<List<TrainingResponse>> getTrainerTrainings(@PathVariable String username,
                                                            @RequestParam(required = false) LocalDate fromDate,
                                                            @RequestParam(required = false) LocalDate toDate,
                                                            @RequestParam(required = false) String traineeName);


    @Operation(summary = "Update trainer", description = "Update an existing trainer's details by username")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainer updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainer not found")
    ApiResponse<TrainerResponse> update(@PathVariable String username,@Valid @RequestBody TrainerRequest request);


    @Operation(summary = "Update trainer password", description = "Change the password of a trainer")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password updated successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Old password is incorrect")
    ApiResponse<Void> updatePassword(@RequestParam String username,
                                            @RequestParam String oldPassword,
                                            @RequestParam String newPassword);


    @Operation(summary = "Deactivate a trainer", description = "Deactivate a trainer by username")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainer deactivated successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainer not found")
    ApiResponse<Void> deActivateUser(@RequestParam String username);


    @Operation(summary = "Activate a trainer", description = "Activate a trainer by username")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trainer activated successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainer not found")
    ApiResponse<Void> activateUser(@RequestParam String username);

}
