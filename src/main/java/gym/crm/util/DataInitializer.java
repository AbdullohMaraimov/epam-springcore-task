package gym.crm.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.dto.request.TrainingRequest;
import gym.crm.model.TrainingType;
import gym.crm.service.TraineeService;
import gym.crm.service.TrainerService;
import gym.crm.service.TrainingService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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

    @Value("${storage.trainee.file}")
    private String traineeDataFile;

    @Value("${storage.trainer.file}")
    private String trainerDataFile;

    @Value("${storage.training.file}")
    private String trainingDataFile;

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

        @PostConstruct
        public void initAll() {
            log.info("Initializing data...");
            initTrainee();
            initTrainer();
            initTraining();
            log.info("Data initialization complete.");
        }

    public void initTrainee() {
        log.info("Initializing trainee data from file...");
        Resource resource = new ClassPathResource("trainee-data.csv");

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> rows = reader.readAll();
            for (String[] parts : rows) {
                if (parts.length >= 5) {
                    String firstName = parts[0];
                    String lastName = parts[1];
                    LocalDate dateOfBirth = LocalDate.parse(parts[2], FORMATTER);
                    String address = parts[3];
                    boolean isActive = Boolean.parseBoolean(parts[4]);

                    TraineeRequest trainee = new TraineeRequest(
                            firstName,
                            lastName,
                            dateOfBirth,
                            address,
                            isActive
                    );
                    traineeService.create(trainee);
                    log.info("Trainee created: {}", trainee);
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
        Resource resource = new ClassPathResource("trainer-data.csv");

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> rows = reader.readAll();
            for (String[] parts : rows) {
                if (parts.length >= 3) {
                    String firstName = parts[0];
                    String lastName = parts[1];
                    String specialization = parts[2];

                    TrainerRequest trainer = new TrainerRequest(
                            firstName,
                            lastName,
                            specialization
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
        Resource resource = new ClassPathResource("training-data.csv");
        Resource resource2 = new ClassPathResource("this-mock-data"); // todo to be deleted

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> rows = reader.readAll();
            for (String[] parts : rows) {
                if (parts.length >= 6) {
                    String traineeId = parts[0];
                    String trainerId = parts[1];
                    String trainingName = parts[2];
                    TrainingType trainingType = TrainingType.valueOf(parts[3]);
                    LocalDate trainingDate = LocalDate.parse(parts[4], FORMATTER);
                    Duration duration = Duration.parse(parts[5]);

                    TrainingRequest training = new TrainingRequest(
                            traineeId,
                            trainerId,
                            trainingName,
                            trainingType,
                            trainingDate,
                            duration
                    );
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
