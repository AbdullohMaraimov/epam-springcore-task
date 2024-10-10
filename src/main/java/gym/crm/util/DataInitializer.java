package gym.crm.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.dto.request.TrainingRequest;
import gym.crm.dto.request.TrainingTypeRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.mapper.TraineeMapper;
import gym.crm.model.Trainee;
import gym.crm.model.Trainer;
import gym.crm.model.TrainingType;
import gym.crm.repository.TraineeRepository;
import gym.crm.repository.TrainerRepository;
import gym.crm.service.TraineeService;
import gym.crm.service.TrainerService;
import gym.crm.service.TrainingService;
import gym.crm.service.TrainingTypeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${storage.trainee.file}")
    private String traineeDataFile;

    @Value("${storage.trainer.file}")
    private String trainerDataFile;

    @Value("${storage.training.file}")
    private String trainingDataFile;

    @Value("${storage.training-type.file}")
    private String trainingTypeDataFile;

    private final TraineeMapper traineeMapper;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    @PostConstruct
    public void initAll() throws InterruptedException {
        log.info("Initializing data...");
        initTrainingType();
        initTrainer();
        initTrainee();
        initTraining();
        log.info("Data initialization complete.");
    }

    public void initTrainingType() {
        log.info("Initializing training type data from file...");
        Resource resource = new ClassPathResource(trainingTypeDataFile);

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> rows = reader.readAll();
            for (String[] parts : rows) {
                if (parts.length == 1) {
                    String trainingTypeName = parts[0];

                    TrainingTypeRequest trainingTypeRequest = new TrainingTypeRequest(trainingTypeName);

                    trainingTypeService.createTrainingType(trainingTypeRequest);
                    log.info("Training type created: {}", trainingTypeName);
                } else {
                    log.info("Skipping invalid trainee row: {}", String.join(",", parts));
                }
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public void initTrainee() {
        log.info("Initializing trainee data from file...");
        Resource resource = new ClassPathResource(traineeDataFile);

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> rows = reader.readAll();
            for (String[] parts : rows) {
                if (parts.length >= 5) {
                    String firstName = parts[0];
                    String lastName = parts[1];
                    LocalDate dateOfBirth = LocalDate.parse(parts[2], FORMATTER);
                    String address = parts[3];
                    boolean isActive = Boolean.parseBoolean(parts[4]);

                    TraineeRequest traineeRequest = new TraineeRequest(
                            firstName,
                            lastName,
                            dateOfBirth,
                            address,
                            isActive
                    );

                    Trainee trainee = traineeMapper.toTrainee(traineeRequest);
                    trainee.setPassword(passwordEncoder.encode(PasswordGenerator.generatePassword()));

                    traineeRepository.save(trainee);
                    log.info("Trainee created with username: {}", trainee.getUsername());
                } else {
                    log.info("Skipping invalid trainee row: {}", String.join(",", parts));
                }
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public void initTrainer() {
        log.info("Initializing trainer data from file...");
        Resource resource = new ClassPathResource(trainerDataFile);

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> rows = reader.readAll();
            for (String[] parts : rows) {
                if (parts.length >= 3) {
                    String firstName = parts[0];
                    String lastName = parts[1];
                    String specializationName = parts[2];
                    boolean isActive = Boolean.parseBoolean(parts[3]);

                    TrainingType trainingType = trainingTypeService.findByName(specializationName);

                    TrainerRequest trainer = new TrainerRequest(
                            firstName,
                            lastName,
                            trainingType.getId(),
                            isActive
                    );
                    trainerService.create(trainer);
                    log.info("Trainer created: {}", trainer);
                } else {
                    log.warn("Skipping invalid trainer row: {}", String.join(",", parts));
                }
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public void initTraining() {
        log.info("Initializing training data from file");
        Resource resource = new ClassPathResource(trainingDataFile);

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> rows = reader.readAll();
            for (String[] parts : rows) {
                if (parts.length >= 6) {
                    String traineeUsername = parts[0];
                    String trainerUsername = parts[1];
                    String trainingName = parts[2];
                    String trainingTypeName = parts[3];
                    LocalDate trainingDate = LocalDate.parse(parts[4], FORMATTER);
                    Duration duration = Duration.parse(parts[5]);

                    TrainingType trainingType = trainingTypeService.findByName(trainingTypeName);

                    Trainer trainer = trainerRepository.findByUsername(trainerUsername)
                            .orElseThrow(() -> new CustomNotFoundException("Trainer not found"));

                    Trainee trainee = traineeRepository.findByUsername(traineeUsername)
                            .orElseThrow(() -> new CustomNotFoundException("Trainee not found"));

                    TrainingRequest training = new TrainingRequest(
                            trainee.getId(),
                            trainer.getId(),
                            trainingName,
                            trainingType.getId(),
                            trainingDate,
                            duration
                    );

                    log.info("creating training: {}", training);
                    trainingService.create(training);
                    log.info("training created: {}", training);
                } else {
                    log.warn("Skipping invalid training row: {}", String.join(",", parts));
                }
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }
}
