package gym.crm.controller.documentation;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainingRequest;
import gym.crm.model.TrainingType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TrainingControllerDocumentation {

    @Operation(summary = "Create a new training", description = "This endpoint creates a new training session by providing a valid TrainingRequest")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Training created successfully",
                    content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request due to invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainer or Trainee not found")
    })
    ApiResponse<Void> create(@Valid @RequestBody TrainingRequest request);


    @Operation(summary = "Find a training by ID",
            description = "Retrieve the details of a specific training using the training ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully found the training",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Training not found with provided ID")
    })
    ApiResponse<TrainingResponse> findById(@PathVariable Long id);


    @Operation(summary = "Retrieve all trainings",
            description = "Get a list of all the trainings available in the system.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved all trainings",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No trainings found")
    })
    ApiResponse<List<TrainingResponse>> findAll();


    @Operation(summary = "Retrieve all training types",
            description = "Get a list of all available training types in the system.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved all training types",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingType.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No training types found",
                    content = @Content(mediaType = "application/json"))
    })
    ApiResponse<List<TrainingType>> findAllTrainingTypes();

}
