package gym_crm.mapper;

import gym_crm.dto.reponse.TrainerResponse;
import gym_crm.dto.request.TrainerRequest;
import gym_crm.model.Trainer;
import gym_crm.util.PasswordGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrainerMapper {

    public Trainer toTrainer(TrainerRequest request) {
        Trainer trainer = new Trainer();
        trainer.setUserId(UUID.randomUUID().toString());
        trainer.setFirstName(request.firstName());
        trainer.setLastName(request.lastName());
        trainer.setSpecialization(request.specialization());
        trainer.setIsActive(true);
        trainer.setUsername(request.firstName().concat(".").concat(request.lastName()));
        trainer.setPassword(PasswordGenerator.generatePassword());
        return trainer;
    }

    public TrainerResponse toTrainerResponse(Trainer trainer) {
        return new TrainerResponse(
                trainer.getUserId(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getUsername(),
                trainer.getSpecialization(),
                trainer.getIsActive()
        );
    }

    public List<TrainerResponse> toTrainerResponses(List<Trainer> trainers) {
        List<TrainerResponse> trainerResponses = new ArrayList<>();
        for (Trainer trainer : trainers) {
            trainerResponses.add(toTrainerResponse(trainer));
        }
        return trainerResponses;
    }

    public Trainer toUpdatedTrainer(Trainer trainer, TrainerRequest trainerRequest) {
        trainer.setFirstName(trainerRequest.firstName());
        trainer.setLastName(trainerRequest.lastName());
        trainer.setSpecialization(trainerRequest.specialization());
        return trainer;
    }
}
