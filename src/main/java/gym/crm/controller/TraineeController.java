package gym.crm.controller;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TraineeResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.service.TraineeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;

    @PostMapping
    public ApiResponse<Void> create(@RequestBody TraineeRequest request) {
        log.info("Creating trainee: {}", request);
        return traineeService.create(request);
    }

    @PutMapping("/{username}")
    public ApiResponse<Void> update(@PathVariable String username, @RequestBody TraineeRequest request) {
        log.info("Updating trainee with username {}: {}", username, request);
        return traineeService.update(username, request);
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
