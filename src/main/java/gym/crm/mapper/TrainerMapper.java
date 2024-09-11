package gym.crm.mapper;

import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.model.Trainer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrainerMapper {

    public Trainer toTrainer(TrainerRequest request) {
        Trainer trainer = new Trainer();
        trainer.setFirstName(request.firstName());
        trainer.setLastName(request.lastName());
        trainer.setIsActive(true);
        trainer.setUsername(request.firstName().concat(".").concat(request.lastName()));
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
        return trainer;
    }
}
