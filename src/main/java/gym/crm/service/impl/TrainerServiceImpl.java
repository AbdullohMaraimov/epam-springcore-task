package gym.crm.service.impl;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.mapper.TrainerMapper;
import gym.crm.model.Trainer;
import gym.crm.repository.TrainerDAO;
import gym.crm.service.TrainerService;
import gym.crm.util.PasswordGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerMapper trainerMapper;

    private final TrainerDAO trainerDAO;

    @Override
    @Transactional
    public ApiResponse<Void> create(TrainerRequest trainerRequest) {
        log.debug("Creating new trainer with request: {}", trainerRequest);
        Trainer trainer = trainerMapper.toTrainer(trainerRequest);
        trainer.setPassword(PasswordGenerator.generatePassword());
        if (trainerDAO.isUsernameExists(trainer.getUsername())) {
            trainer.setUsername(trainer.getUsername() + TrainerDAO.index++);
            log.info("Username already exists, changed it to {}", trainer.getUsername());
        }
        trainerDAO.save(trainer);
        log.info("Trainer saved successfully: {}", trainer);
        return new ApiResponse<>(200, "Saved successfully!", true);
    }

    @Override
    @Transactional
    public ApiResponse<Void> update(String username, TrainerRequest trainerRequest) {
        log.debug("Updating trainer with username: {}", username);
        Trainer trainer = trainerDAO.findByUsername(username);
        if (trainer == null) {
            log.error("Trainer with username %s not found".formatted(username));
            throw new CustomNotFoundException("Trainer not found!");
        }
        Trainer updated = trainerMapper.toUpdatedTrainer(trainer, trainerRequest);
        trainerDAO.update(updated);
        log.info("Trainer updated successfully: {}", updated);
        return new ApiResponse<>(200,true, null, "Successfully updated!");
    }

    @Override
    @Transactional
    public ApiResponse<Void> updatePassword(String username, String oldPassword, String newPassword) {
        log.debug("Updating password for trainer {}", username);
        Trainer trainer = trainerDAO.findByUsername(username);
        if (trainer == null){
            throw new CustomNotFoundException("Trainer not found!");
        }
        if (!Objects.equals(trainer.getPassword(), oldPassword)) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        trainer.setPassword(newPassword);
        trainerDAO.save(trainer);
        log.info("Password updated successfully for trainer {}", username);
        return new ApiResponse<>(200, "Password update successful", true);
    }

    @Override
    @Transactional
    public ApiResponse<Void> deActivateUser(String username) {
        log.debug("Deactivating trainer with username {}", username);
        Trainer trainer = trainerDAO.findByUsername(username);
        if (trainer == null) {
            log.error("Trainer with username {} not found", username);
            throw new CustomNotFoundException("Trainer not found!");
        }
        if (!trainer.getIsActive()) {
            log.warn("Trainer {} is already inactive", username);
            throw new IllegalArgumentException("Trainer is already inactive");
        }
        trainer.setIsActive(false);
        trainerDAO.save(trainer);
        log.info("Trainer {} deactivated successfully", username);
        return new ApiResponse<>(200, "User deActivated successfully", true);
    }

    @Override
    @Transactional
    public ApiResponse<Void> activateUser(String username) {
        log.debug("Activating trainer with username {}", username);
        Trainer trainer = trainerDAO.findByUsername(username);
        if (trainer == null) {
            log.error("Trainer with username {} not found", username);
            throw new CustomNotFoundException("Trainer not found!");
        }
        if (trainer.getIsActive()) {
            log.warn("Trainer {} is already active", username);
            throw new IllegalArgumentException("Trainer is already active");
        }
        trainer.setIsActive(true);
        trainerDAO.save(trainer);
        log.info("Trainer {} activated successfully", username);
        return new ApiResponse<>(200, "User Activated successfully", true);
    }

    @Override
    public ApiResponse<TrainerResponse> findByUsername(String username) {
        log.debug("Finding trainer with username: {}", username);
        Trainer trainer = trainerDAO.findByUsername(username);
        if (trainer == null) {
            log.error("Trainer with username %s not found".formatted(username));
            throw new CustomNotFoundException("Trainer not found!");
        }
        TrainerResponse trainerResponse = trainerMapper.toTrainerResponse(trainer);
        log.info("Trainer found: {}", trainerResponse);
        return new ApiResponse<>(200,true, trainerResponse, "Successfully found!");
    }

    @Override
    public ApiResponse<List<TrainerResponse>> findAll() {
        log.debug("Finding all trainers");
        List<Trainer> trainers = trainerDAO.findAll();
        if (trainers.isEmpty()) {
            log.error("No trainers found!");
            throw new CustomNotFoundException("Trainer not found!");
        }
        List<TrainerResponse> trainerResponses = trainerMapper.toTrainerResponses(trainers);
        log.info("Found {} trainers", trainerResponses.size());
        return new ApiResponse<>(200,true, trainerResponses, "Success!");
    }
}
