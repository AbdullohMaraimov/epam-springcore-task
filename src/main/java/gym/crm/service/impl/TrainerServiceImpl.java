package gym.crm.service.impl;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.mapper.TrainerMapper;
import gym.crm.model.Trainer;
import gym.crm.repository.TrainerDAO;
import gym.crm.service.TrainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerMapper trainerMapper;

    private final TrainerDAO trainerDAO;

    @Override
    public ApiResponse<Void> create(TrainerRequest trainerRequest) {
        log.debug("Creating new trainer with request: {}", trainerRequest);
        Trainer trainer = trainerMapper.toTrainer(trainerRequest);
        if (trainerDAO.isUsernameExists(trainer.getUsername())) {
            trainer.setUsername(trainer.getUsername() + TrainerDAO.index++);
            trainerDAO.save(trainer);
            log.info("Username already exists, changed it to {}", trainer.getUsername());
            return new ApiResponse<>(204, "Username already exists, so changed it to " + trainer.getUsername(), true);
        } else {
            trainerDAO.save(trainer);
            log.info("Trainer saved successfully: {}", trainer);
            return new ApiResponse<>(204, "Saved successfully!", true);
        }
    }

    @Override
    public ApiResponse<Void> update(String username, TrainerRequest trainerRequest) {
        log.debug("Updating trainer with username: {}", username);
        if (trainerDAO.isUsernameExists(username)) {
            Trainer trainer = trainerDAO.findByUsername(username);
            Trainer updated = trainerMapper.toUpdatedTrainer(trainer, trainerRequest);
            trainerDAO.update(updated);
            log.info("Trainer updated successfully: {}", updated);
            return new ApiResponse<>(204,true, null, "Successfully updated!");
        } else {
            log.error("Trainer with username " + username + " not found");
            throw new CustomNotFoundException("Trainer not found!");
        }
    }

    @Override
    public ApiResponse<TrainerResponse> findByUsername(String username) {
        log.debug("Finding trainer with username: {}", username);
        if (trainerDAO.isUsernameExists(username)) {
            Trainer trainer = trainerDAO.findByUsername(username);
            TrainerResponse trainerResponse = trainerMapper.toTrainerResponse(trainer);
            log.info("Trainer found: {}", trainerResponse);
            return new ApiResponse<>(200,true, trainerResponse, "Successfully found!");
        } else {
            log.error("Trainer with username " + username + " not found");
            throw new CustomNotFoundException("Trainer not found!");
        }
    }

    @Override
    public ApiResponse<List<TrainerResponse>> findAll() {
        log.debug("Finding all trainers");
        if (trainerDAO.isTrainerDBEmpty()) {
            log.error("No trainers found!");
            throw new CustomNotFoundException("Trainer not found!");
        } else {
            List<Trainer> trainers = trainerDAO.findAll();
            List<TrainerResponse> trainerResponses = trainerMapper.toTrainerResponses(trainers);
            log.info("Found {} trainers", trainerResponses.size());
            return new ApiResponse<>(200,true, trainerResponses, "Success!");
        }
    }
}
