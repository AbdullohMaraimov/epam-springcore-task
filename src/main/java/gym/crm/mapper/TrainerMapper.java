package gym.crm.mapper;

import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.model.Trainer;
import gym.crm.model.TrainingType;
import gym.crm.repository.TrainingTypeDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrainerMapper {

    private final TrainingTypeDAO trainingTypeDAO;

    public Trainer toTrainer(TrainerRequest request) {
        Trainer trainer = new Trainer();
        trainer.setFirstName(request.firstName());
        trainer.setLastName(request.lastName());
        trainer.setIsActive(true);
        trainer.setUsername(request.firstName().concat(".").concat(request.lastName()));
        TrainingType trainingType = trainingTypeDAO.findById(request.specializationId());
        if (trainingType == null) {
            throw new CustomNotFoundException("TrainingType with id : %d not found".formatted(request.specializationId()));
        }
        trainer.setSpecialization(trainingType);
        return trainer;
    }

    public TrainerResponse toTrainerResponse(Trainer trainer) {
        return new TrainerResponse(
                trainer.getId(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getUsername(),
                trainer.getSpecialization().getName(),
                trainer.getIsActive()
        );
    }

    public List<TrainerResponse> toTrainerResponses(List<Trainer> trainers) {
        return trainers.stream()
                .map(this::toTrainerResponse)
                .toList();
    }

    public Trainer toUpdatedTrainer(Trainer trainer, TrainerRequest trainerRequest) {
        trainer.setFirstName(trainerRequest.firstName());
        trainer.setLastName(trainerRequest.lastName());
        TrainingType trainingType = trainingTypeDAO.findById(trainerRequest.specializationId());
        if (trainingType == null) {
            throw new CustomNotFoundException("TrainingType with id : %d not found".formatted(trainerRequest.specializationId()));
        }
        trainer.setSpecialization(trainingType);
        return trainer;
    }
}
