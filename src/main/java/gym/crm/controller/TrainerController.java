package gym.crm.controller;

import gym.crm.controller.documentation.TrainerControllerDocumentation;
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
public class TrainerController implements TrainerControllerDocumentation {

    private final TrainerService trainerService;
    private final TrainingService trainingService;


    @GetMapping("/{username}")
    public ApiResponse<TrainerResponse> findByUsername(@PathVariable String username) {
        log.info("Finding trainer with username {}", username);
        TrainerResponse trainerResponse = trainerService.findByUsername(username);
        return new ApiResponse<>(200,true, trainerResponse, "Successfully found!");
    }

    @GetMapping
    public ApiResponse<List<TrainerResponse>> findAll() {
        log.info("Finding all trainers");
        List<TrainerResponse> trainerResponses = trainerService.findAll();
        return new ApiResponse<>(200,true, trainerResponses, "Success!");
    }

    @GetMapping("/{username}/trainings")
    public ApiResponse<List<TrainingResponse>> getTrainerTrainings(@PathVariable String username,
                                                                   @RequestParam(required = false) LocalDate fromDate,
                                                                   @RequestParam(required = false) LocalDate toDate,
                                                                   @RequestParam(required = false) String traineeName) {
        log.info("Getting trainings for trainer {} from {} to {}, trainee: {}",
                username, fromDate, toDate, traineeName);

        List<TrainingResponse> responses = trainingService.getTrainingsByTrainer(username, fromDate, toDate, traineeName);
        return new ApiResponse<>(200, true, responses, "Successfully found!");
    }

    @PutMapping("/{username}")
    public ApiResponse<TrainerResponse> update(@PathVariable String username,@Valid @RequestBody TrainerRequest request) {
        log.info("Updating trainer with username {}: {}", username, request);
        TrainerResponse trainerResponse = trainerService.update(username, request);
        return new ApiResponse<>(200,true, trainerResponse, "Successfully updated!");
    }

    @PatchMapping("/update-password")
    public ApiResponse<Void> updatePassword(@RequestParam String username,
                                            @RequestParam String oldPassword,
                                            @RequestParam String newPassword) {
        log.info("Updating password for trainer {}", username);
        trainerService.updatePassword(username, oldPassword, newPassword);
        return new ApiResponse<>(200, "Password update successful", true);
    }

    @PatchMapping("/de-activate")
    public ApiResponse<Void> deActivateUser(@RequestParam String username) {
        log.info("Deactivating trainer {}", username);
        trainerService.deActivateUser(username);
        return new ApiResponse<>(200, "User deActivated successfully", true);
    }

    @PatchMapping("/activate")
    public ApiResponse<Void> activateUser(@RequestParam String username) {
        log.info("Activating trainer {}", username);
        trainerService.activateUser(username);
        return new ApiResponse<>(200, "User Activated successfully", true);
    }

}
