package gym_crm.controller;

import gym_crm.dto.reponse.ApiResponse;
import gym_crm.dto.reponse.TrainerResponse;
import gym_crm.dto.request.TrainerRequest;
import gym_crm.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @PostMapping
    public ApiResponse<Void> create(@RequestBody TrainerRequest request) {
        return trainerService.create(request);
    }

    @GetMapping("/{username}")
    public ApiResponse<TrainerResponse> findByUsername(@PathVariable String username) {
        return trainerService.findByUsername(username);
    }

    @GetMapping
    public ApiResponse<List<TrainerResponse>> findAll() {
        return trainerService.findAll();
    }

    @PutMapping("/{username}")
    public ApiResponse<Void> update(@PathVariable String username, @RequestBody TrainerRequest request) {
        return trainerService.update(username, request);
    }

}
