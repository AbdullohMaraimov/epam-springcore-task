package gym_crm.util;

import gym_crm.dto.request.TraineeRequest;
import gym_crm.dto.request.TrainerRequest;
import gym_crm.dto.request.TrainingRequest;
import gym_crm.model.TrainingType;
import gym_crm.service.TraineeService;
import gym_crm.service.TrainerService;
import gym_crm.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class DataInitializer {

    @Value("${storage.trainee.file}")
    private String traineeDataFile;

    @Value("${storage.trainer.file}")
    private String trainerDataFile;

    @Value("${storage.training.file}")
    private String trainingDataFile;

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public void initAll() {
        initTrainee();
        initTrainer();
        initTraining();
    }

    public void initTrainee() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Resource resource = new ClassPathResource("trainee-data.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String firstName = parts[0];
                String lastName = parts[1];
                LocalDate dateOfBirth = LocalDate.parse(parts[2], formatter);
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
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initTrainer() {
        Resource resource = new ClassPathResource("trainer-data.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String firstName = parts[0];
                String lastName = parts[1];
                String specialization = parts[2];

                TrainerRequest trainer = new TrainerRequest(
                        firstName,
                        lastName,
                        specialization
                );
                trainerService.create(trainer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initTraining() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Resource resource = new ClassPathResource("training-data.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String traineeId = parts[0];
                String trainerId = parts[1];
                String trainingName = parts[2];
                TrainingType trainingType = TrainingType.valueOf(parts[3]);
                LocalDate trainingDate = LocalDate.parse(parts[4], formatter);
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
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
