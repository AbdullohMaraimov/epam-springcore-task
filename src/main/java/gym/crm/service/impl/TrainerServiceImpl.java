package gym.crm.service.impl;

import gym.crm.dto.reponse.RegistrationResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.mapper.TrainerMapper;
import gym.crm.model.Trainer;
import gym.crm.model.TrainingType;
import gym.crm.repository.TrainerRepository;
import gym.crm.repository.TrainingTypeRepository;
import gym.crm.service.TrainerService;
import gym.crm.util.PasswordGenerator;
import gym.crm.util.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerMapper trainerMapper;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RegistrationResponse create(TrainerRequest trainerRequest) {
        log.debug("Creating new trainer with request: {}", trainerRequest);
        Trainer trainer = trainerMapper.toTrainer(trainerRequest);
        String generatedPassword = PasswordGenerator.generatePassword();
        log.info("Password generated: {}", generatedPassword);
        trainer.setPassword(passwordEncoder.encode(generatedPassword));
        TrainingType trainingType = trainingTypeRepository.findById(trainerRequest.specializationId())
                .orElseThrow(() -> new CustomNotFoundException("TrainingType with id : %d not found".formatted(trainerRequest.specializationId())));

        trainer.setSpecialization(trainingType);
        if (trainerRepository.existsTrainerByUsername(trainer.getUsername())) {
            trainer.setUsername(trainer.getUsername() +  Utils.trainerIdx++);
            log.info("Username already exists, changed it to {}", trainer.getUsername());
        }
        RegistrationResponse registrationResponse = new RegistrationResponse(trainer.getUsername(), trainer.getPassword());
        trainerRepository.save(trainer);
        log.info("Trainer saved successfully: {}", trainer);
        return registrationResponse;
    }

    @Override
    @Transactional
    public TrainerResponse update(String username, TrainerRequest trainerRequest) {
        log.debug("Updating trainer with username: {}", username);
        Trainer trainer = trainerRepository.findByUsername (username)
                .orElseThrow(() -> new CustomNotFoundException("Trainer not found!"));

        Trainer updated = trainerMapper.toUpdatedTrainer(trainer, trainerRequest);
        TrainingType trainingType = trainingTypeRepository.findById(trainerRequest.specializationId())
                .orElseThrow(() -> new CustomNotFoundException("TrainingType with id : %d not found".formatted(trainerRequest.specializationId())));

        updated.setSpecialization(trainingType);
        trainerRepository.save(updated);

        TrainerResponse trainerResponse = trainerMapper.toTrainerResponse(updated);

        log.info("Trainer updated successfully: {}", updated);
        return trainerResponse;
    }

    @Override
    @Transactional
    public void updatePassword(String username, String oldPassword, String newPassword) {
        log.debug("Updating password for trainer {}", username);
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("Trainer not found!"));

        if (!Objects.equals(trainer.getPassword(), oldPassword)) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        trainer.setPassword(newPassword);
        trainerRepository.save(trainer);
        log.info("Password updated successfully for trainer {}", username);
    }

    @Override
    @Transactional
    public void deActivateUser(String username) {
        log.debug("Deactivating trainer with username {}", username);
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("Trainer not found!"));

        if (!trainer.getIsActive()) {
            log.warn("Trainer {} is already inactive", username);
            throw new IllegalArgumentException("Trainer is already inactive");
        }
        trainer.setIsActive(false);
        trainerRepository.save(trainer);
        log.info("Trainer {} deactivated successfully", username);
    }

    @Override
    @Transactional
    public void activateUser(String username) {
        log.debug("Activating trainer with username {}", username);
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("Trainer not found!"));

        if (trainer.getIsActive()) {
            log.warn("Trainer {} is already active", username);
            throw new IllegalArgumentException("Trainer is already active");
        }
        trainer.setIsActive(true);
        trainerRepository.save(trainer);
        log.info("Trainer {} activated successfully", username);
    }

    @Override
    public TrainerResponse findByUsername(String username) {
        log.debug("Finding trainer with username: {}", username);
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("Trainer not found!"));

        TrainerResponse trainerResponse = trainerMapper.toTrainerResponse(trainer);
        log.info("Trainer found: {}", trainerResponse);
        return trainerResponse;
    }

    @Override
    public List<TrainerResponse> findAll() {
        log.debug("Finding all trainers");
        List<Trainer> trainers = trainerRepository.findAll();
        if (trainers.isEmpty()) {
            log.error("No trainers found!");
            throw new CustomNotFoundException("Trainers not found!");
        }
        List<TrainerResponse> trainerResponses = trainerMapper.toTrainerResponses(trainers);
        log.info("Found {} trainers", trainerResponses.size());
        return trainerResponses;
    }


}
