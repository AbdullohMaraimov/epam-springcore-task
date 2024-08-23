package gym_crm.service.impl;

import gym_crm.dto.reponse.ApiResponse;
import gym_crm.dto.reponse.TrainerResponse;
import gym_crm.dto.request.TrainerRequest;
import gym_crm.mapper.TrainerMapper;
import gym_crm.model.Trainer;
import gym_crm.repository.TrainerDAO;
import gym_crm.service.TrainerService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerMapper trainerMapper;

    private final TrainerDAO trainerDAO;

    @Override
    public ApiResponse<Void> create(TrainerRequest trainerRequest) {
        Trainer trainer = trainerMapper.toTrainer(trainerRequest);
        if (trainerDAO.isUsernameExists(trainer.getUsername())) {
            trainer.setUsername(trainer.getUsername() + TrainerDAO.index++);
            trainerDAO.save(trainer);
            return new ApiResponse<>(true, null, "Username already exists, so changed it to " + trainer.getUsername());
        } else {
            trainerDAO.save(trainer);
            return new ApiResponse<>(true, null, "Saved successfully!");
        }
    }

    @Override
    public ApiResponse<Void> update(String username, TrainerRequest trainerRequest) {
        if (trainerDAO.isUsernameExists(username)) {
            Trainer trainer = trainerDAO.findByUsername(username);
            Trainer updated = trainerMapper.toUpdatedTrainer(trainer, trainerRequest);
            trainerDAO.update(updated);
            return new ApiResponse<>(true, null, "Successfully updated!");
        } else {
            return new ApiResponse<>(false, null, "Trainer not found!");
        }
    }

    @Override
    public ApiResponse<TrainerResponse> findByUsername(String username) {
        if (trainerDAO.isUsernameExists(username)) {
            Trainer trainer = trainerDAO.findByUsername(username);
            TrainerResponse trainerResponse = trainerMapper.toTrainerResponse(trainer);
            return new ApiResponse<>(true, trainerResponse, "Successfully found!");
        } else {
            return new ApiResponse<>(false, null , "Trainer not found!");
        }
    }

    @Override
    public ApiResponse<List<TrainerResponse>> findAll() {
        if (trainerDAO.isTrainerDBEmpty()) {
            return new ApiResponse<>(false, null, "No Trainer found!");
        } else {
            List<Trainer> trainers = trainerDAO.findAll();
            List<TrainerResponse> trainerResponses = trainerMapper.toTrainerResponses(trainers);
            return new ApiResponse<>(true, trainerResponses, "Success!");
        }
    }
}
