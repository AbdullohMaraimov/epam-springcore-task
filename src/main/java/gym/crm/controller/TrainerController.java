package gym.crm.controller;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.service.TrainerService;
import gym.crm.service.TrainingService;
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

    @PostMapping
    public ApiResponse<Void> create(@Valid @RequestBody TrainerRequest request) {
        log.info("Creating trainer: {}", request);
        return trainerService.create(request);
    }

    @GetMapping("/{username}")
    public ApiResponse<TrainerResponse> findByUsername(@PathVariable String username) {
        log.info("Finding trainer with username {}", username);
        return trainerService.findByUsername(username);
    }

    @GetMapping
    public ApiResponse<List<TrainerResponse>> findAll() {
        log.info("Finding all trainers");
        return trainerService.findAll();
    }

    @GetMapping("/{username}/trainings")
    public ApiResponse<List<TrainingResponse>> getTrainerTrainings(@PathVariable String username,
                                                                   @RequestParam(required = false) LocalDate fromDate,
                                                                   @RequestParam(required = false) LocalDate toDate,
                                                                   @RequestParam(required = false) String traineeName) {
        log.info("Getting trainings for trainer {} from {} to {}, trainee: {}",
                username, fromDate, toDate, traineeName);

        return trainingService.getTrainingsByTrainer(username, fromDate, toDate, traineeName);
    }

    @PutMapping("/{username}")
    public ApiResponse<Void> update(@PathVariable String username,@Valid @RequestBody TrainerRequest request) {
        log.info("Updating trainer with username {}: {}", username, request);
        return trainerService.update(username, request);
    }

    @PatchMapping("/update-password")
    public ApiResponse<Void> updatePassword(@RequestParam String username,
                                            @RequestParam String oldPassword,
                                            @RequestParam String newPassword) {
        log.info("Updating password for trainer {}", username);
        return trainerService.updatePassword(username, oldPassword, newPassword);
    }

    @PatchMapping("/de-activate")
    public ApiResponse<Void> deActivateUser(@RequestParam String username) {
        log.info("Deactivating trainer {}", username);
        return trainerService.deActivateUser(username);
    }

    @PatchMapping("/activate")
    public ApiResponse<Void> activateUser(@RequestParam String username) {
        log.info("Activating trainer {}", username);
        return trainerService.activateUser(username);
    }

}
