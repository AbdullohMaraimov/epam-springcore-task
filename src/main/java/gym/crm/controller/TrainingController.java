package gym.crm.controller;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainingRequest;
import gym.crm.model.TrainingType;
import gym.crm.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;


    @Operation(summary = "Create a new training", description = "This endpoint creates a new training session by providing a valid TrainingRequest")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Training created successfully",
                    content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request due to invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trainer or Trainee not found")
    })
    @PostMapping
    public ApiResponse<Void> create(@Valid @RequestBody TrainingRequest request) {
        log.info("Creating training: {}", request);
        trainingService.create(request);
        log.info("Created training: {}", request);
        return new ApiResponse<>(200,true, null, "Training created successfully!");
    }


    @Operation(summary = "Find a training by ID",
            description = "Retrieve the details of a specific training using the training ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully found the training",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Training not found with provided ID")
    })
    @GetMapping("{id}")
    public ApiResponse<TrainingResponse> findById(@PathVariable Long id) {
        log.info("Finding training with ID {}", id);
        TrainingResponse response = trainingService.findById(id);
        return new ApiResponse<>(200, true, response, "Successfully found");
    }


    @Operation(summary = "Retrieve all trainings",
            description = "Get a list of all the trainings available in the system.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved all trainings",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No trainings found")
    })
    @GetMapping
    public ApiResponse<List<TrainingResponse>> findAll() {
        log.info("Finding all trainings");
        List<TrainingResponse> responses = trainingService.findAll();
        return new ApiResponse<>(200,true, responses, "Success!");
    }


    @Operation(summary = "Retrieve all training types",
            description = "Get a list of all available training types in the system.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved all training types",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingType.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No training types found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/type")
    public ApiResponse<List<TrainingType>> findAllTrainingTypes() {
        log.info("Finding all training types");
        List<TrainingType> trainingTypes = trainingService.findAllTrainingTypes();
        return new ApiResponse<>(200, true, trainingTypes, "Successfully found!");
    }

}
