package gym.crm.controller;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainingRequest;
import gym.crm.service.TrainingService;
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

    @PostMapping
    public ApiResponse<Void> create(@RequestBody TrainingRequest request) {
        log.info("Creating training: {}", request);
        return trainingService.create(request);
    }

    @GetMapping("{id}")
    public ApiResponse<TrainingResponse> findById(@PathVariable String id) {
        log.info("Finding training with ID {}", id);
        return trainingService.findById(id);
    }

    @GetMapping
    public ApiResponse<List<TrainingResponse>> findAll() {
        log.info("Finding all trainings");
        return trainingService.findAll();
    }

}
