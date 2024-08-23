package gym_crm.controller;

import gym_crm.dto.reponse.ApiResponse;
import gym_crm.dto.reponse.TrainingResponse;
import gym_crm.dto.request.TrainingRequest;
import gym_crm.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping
    public ApiResponse<Void> create(@RequestBody TrainingRequest request) {
        return trainingService.create(request);
    }

    @GetMapping("{id}")
    public ApiResponse<TrainingResponse> findById(@PathVariable String id) {
        return trainingService.findById(id);
    }

    @GetMapping
    public ApiResponse<List<TrainingResponse>> findAll() {
        return trainingService.findAll();
    }

}
