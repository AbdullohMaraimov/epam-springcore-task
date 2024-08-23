package gym_crm.controller;

import gym_crm.dto.reponse.ApiResponse;
import gym_crm.dto.reponse.TraineeResponse;
import gym_crm.dto.request.TraineeRequest;
import gym_crm.service.TraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;

    @PostMapping
    public ApiResponse<Void> create(@RequestBody TraineeRequest request) {
        return traineeService.create(request);
    }

    @PutMapping("/{username}")
    public ApiResponse<Void> update(@PathVariable String username, @RequestBody TraineeRequest request) {
        return traineeService.update(username, request);
    }

    @GetMapping("/{username}")
    public ApiResponse<TraineeResponse> findByUsername(@PathVariable String username) {
        return traineeService.findByUsername(username);
    }

    @GetMapping
    public ApiResponse<List<TraineeResponse>> findAll() {
        return traineeService.findAll();
    }

    @DeleteMapping("/{username}")
    public ApiResponse<Void> deleteByUsername(@PathVariable String username) {
        return traineeService.delete(username);
    }

    @DeleteMapping
    public ApiResponse<Void> deleteAll() {
        return traineeService.deleteAll();
    }

}
