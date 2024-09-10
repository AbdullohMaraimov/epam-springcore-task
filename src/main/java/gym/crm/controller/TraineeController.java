package gym.crm.controller;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TraineeResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.model.TrainingType;
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
public class TraineeController {

    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @PatchMapping("/{username}")
    public ApiResponse<Void> update(@PathVariable String username, @Valid @RequestBody TraineeRequest request) {
        log.info("Updating trainee with username {}: {}", username, request);
        return traineeService.update(username, request);
    }

    @PatchMapping("/update-password")
    public ApiResponse<Void> updatePassword(@RequestParam String username,
                                            @RequestParam String oldPassword,
                                            @RequestParam String newPassword) {
        log.info("Updating password for username {}", username);
        return traineeService.updatePassword(username, oldPassword, newPassword);
    }

    @PatchMapping("/de-activate")
    public ApiResponse<Void> deActivateUser(@RequestParam String username) {
        log.info("Deactivating user with username {}", username);
        return traineeService.deActivateUser(username);
    }

    @PatchMapping("/activate")
    public ApiResponse<Void> activateUser(@RequestParam String username) {
        log.info("Activating user with username {}", username);
        return traineeService.activateUser(username);
    }

    @GetMapping("/{username}")
    public ApiResponse<TraineeResponse> findByUsername(@PathVariable String username) {
        log.info("Finding trainee with username {}", username);
        return traineeService.findByUsername(username);
    }

    @GetMapping
    public ApiResponse<List<TraineeResponse>> findAll() {
        log.info("Finding all trainees");
        return traineeService.findAll();
    }

    @GetMapping("/{username}/trainings")
    public ApiResponse<List<TrainingResponse>> getTraineeTrainings(@PathVariable String username,
                                                                   @RequestParam(required = false) LocalDate fromDate,
                                                                   @RequestParam(required = false) LocalDate toDate,
                                                                   @RequestParam(required = false) String trainerUsername,
                                                                   @RequestParam(required = false) Long trainingTypeId) {
        log.info("Finding trainings for trainee {} from {} to {} with name {} and type {}", username, fromDate, toDate, trainerUsername, trainingTypeId);
        return trainingService.findTraineeTrainings(username, fromDate, toDate, trainerUsername, trainingTypeId);
    }

    @GetMapping("/{username}/unassigned-trainers")
    public ApiResponse<List<TrainerResponse>> getAllUnassignedTrainers(@PathVariable String username) {
        log.info("Finding all unassigned trainers for trainee {}", username);
        return traineeService.findAllUnassignedTrainers(username);
    }

    @DeleteMapping("/{username}")
    public ApiResponse<Void> deleteByUsername(@PathVariable String username) {
        log.info("Deleting trainee with username {}", username);
        return traineeService.delete(username);
    }

    @DeleteMapping
    public ApiResponse<Void> deleteAll() {
        log.info("Deleting all trainees");
        return traineeService.deleteAll();
    }

}
