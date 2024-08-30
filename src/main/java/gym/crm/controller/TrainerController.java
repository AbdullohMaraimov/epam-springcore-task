package gym.crm.controller;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.service.TrainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @PostMapping
    public ApiResponse<Void> create(@RequestBody TrainerRequest request) {
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

    @PutMapping("/{username}")
    public ApiResponse<Void> update(@PathVariable String username, @RequestBody TrainerRequest request) {
        log.info("Updating trainer with username {}: {}", username, request);
        return trainerService.update(username, request);
    }

}
