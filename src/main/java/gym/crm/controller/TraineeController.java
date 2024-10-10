package gym.crm.controller;

import gym.crm.controller.documentation.TraineeControllerDocumentation;
import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TraineeResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.service.TraineeService;
import gym.crm.service.TrainingService;
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
public class TraineeController implements TraineeControllerDocumentation {

    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @PatchMapping("/{username}")
    public ApiResponse<TraineeResponse> update(@PathVariable String username, @Valid @RequestBody TraineeRequest request) {
        log.info("Updating trainee with username {}: {}", username, request);
        TraineeResponse traineeResponse = traineeService.update(username, request);
        return new ApiResponse<>(204 , true,   traineeResponse, "Successfully updated!");
    }

    @PatchMapping("/update-password")
    public ApiResponse<Void> updatePassword(@RequestParam String username,
                                            @RequestParam String oldPassword,
                                            @RequestParam String newPassword) {
        log.info("Updating password for username {}", username);
        traineeService.updatePassword(username, oldPassword, newPassword);
        return new ApiResponse<>(200, "Password successfully updated!", true);
    }

    @PatchMapping("/de-activate")
    public ApiResponse<Void> deActivateUser(@RequestParam String username) {
        log.info("Deactivating user with username {}", username);
        traineeService.deActivateUser(username);
        return new ApiResponse<>(200, "User deActivated successfully", true);
    }

    @PatchMapping("/activate")
    public ApiResponse<Void> activateUser(@RequestParam String username) {
        log.info("Activating user with username {}", username);
        traineeService.activateUser(username);
        return new ApiResponse<>(200, "User deActivated successfully", true);
    }

    @GetMapping("/{username}")
    public ApiResponse<TraineeResponse> findByUsername(@PathVariable String username) {
        log.info("Finding trainee with username {}", username);
        TraineeResponse traineeResponse = traineeService.findByUsername(username);
        return new ApiResponse<>(200, true, traineeResponse, "Successfully found!");
    }

    @GetMapping
    public ApiResponse<List<TraineeResponse>> findAll() {
        log.info("Finding all trainees");
        List<TraineeResponse> traineeResponses = traineeService.findAll();
        return new ApiResponse<>(200,true, traineeResponses, "Success!");
    }

    @GetMapping("/{username}/trainings")
    public ApiResponse<List<TrainingResponse>> getTraineeTrainings(@PathVariable String username,
                                                                   @RequestParam(required = false) LocalDate fromDate,
                                                                   @RequestParam(required = false) LocalDate toDate,
                                                                   @RequestParam(required = false) String trainerUsername,
                                                                   @RequestParam(required = false) Long trainingTypeId) {
        log.info("Finding trainings for trainee {} from {} to {} with name {} and type {}", username, fromDate, toDate, trainerUsername, trainingTypeId);
        List<TrainingResponse> responses = trainingService.findTraineeTrainings(username, fromDate, toDate, trainerUsername, trainingTypeId);
        return new ApiResponse<>(200, true, responses, "Successfully found!");
    }

    @GetMapping("/{username}/unassigned-trainers")
    public ApiResponse<List<TrainerResponse>> getAllUnassignedTrainers(@PathVariable String username) {
        log.info("Finding all unassigned trainers for trainee {}", username);
        List<TrainerResponse> trainerResponses = traineeService.findAllUnassignedTrainers(username);
        return new ApiResponse<>(200, true, trainerResponses, "Successfully retrieved");
    }

    @DeleteMapping("/{username}")
    public ApiResponse<Void> deleteByUsername(@PathVariable String username) {
        log.info("Deleting trainee with username {}", username);
        traineeService.delete(username);
        return new ApiResponse<>(200, "Deleted successfully!", true);
    }

    @DeleteMapping
    public ApiResponse<Void> deleteAll() {
        log.info("Deleting all trainees");
        traineeService.deleteAll();
        return new ApiResponse<>( 204,true, null, "All Trainees deleted!");
    }

}
