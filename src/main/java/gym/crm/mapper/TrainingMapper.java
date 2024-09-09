package gym.crm.mapper;

import gym.crm.dto.reponse.TrainingResponse;
import gym.crm.dto.request.TrainingRequest;
import gym.crm.model.Trainee;
import gym.crm.model.Trainer;
import gym.crm.model.Training;
import gym.crm.model.TrainingType;
import gym.crm.repository.TraineeDAO;
import gym.crm.repository.TrainerDAO;
import gym.crm.repository.TrainingTypeDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TrainingMapper  {

    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;
    private final TrainingTypeDAO trainingTypeDAO;

    public Training toEntity(TrainingRequest trainingRequest) {
        Training training = new Training();
        training.setTrainingName(trainingRequest.trainingName());
        training.setTrainingDate(trainingRequest.trainingDate());
        training.setDuration(trainingRequest.duration());

        Trainee trainee = traineeDAO.findById(trainingRequest.traineeId());
        Trainer trainer = trainerDAO.findById(trainingRequest.trainerId());
        TrainingType trainingType = trainingTypeDAO.findById(trainingRequest.trainingTypeId());

        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);

        return training;
    }

    public TrainingResponse toResponse(Training training) {
        return new TrainingResponse(
                training.getId(),
                training.getTrainee().getId(),
                training.getTrainer().getId(),
                training.getTrainingName(),
                training.getTrainingType().getName(),
                training.getTrainingDate(),
                training.getDuration()
        );
    }

    public List<TrainingResponse> toResponses(List<Training> trainings) {
        List<TrainingResponse> trainingResponses = new ArrayList<>();
        for (Training training : trainings) {
            trainingResponses.add(toResponse(training));
        }
        return trainingResponses;
    }
}
