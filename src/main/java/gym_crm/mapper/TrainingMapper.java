package gym_crm.mapper;

import gym_crm.dto.reponse.TrainingResponse;
import gym_crm.dto.request.TrainingRequest;
import gym_crm.model.Training;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrainingMapper {

    public Training toEntity(TrainingRequest trainingRequest) {
        Training training = new Training();
        training.setId(UUID.randomUUID().toString());
        training.setTraineeId(trainingRequest.traineeId());
        training.setTrainerId(trainingRequest.trainerId());
        training.setTrainingName(trainingRequest.trainingName());
        training.setTrainingType(trainingRequest.trainingType());
        training.setTrainingDate(trainingRequest.trainingDate());
        training.setDuration(trainingRequest.duration());
        return training;
    }

    public TrainingResponse toResponse(Training training) {
        return new TrainingResponse(
                training.getId(),
                training.getTraineeId(),
                training.getTrainerId(),
                training.getTrainingName(),
                training.getTrainingType(),
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
