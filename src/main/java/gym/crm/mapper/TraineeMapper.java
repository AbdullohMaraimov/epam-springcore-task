package gym.crm.mapper;

import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.reponse.TraineeResponse;
import gym.crm.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TraineeMapper {

    private TrainerMapper trainerMapper;

    @Autowired
    public void setTrainerMapper(@Lazy TrainerMapper trainerMapper) {
        this.trainerMapper = trainerMapper;
    }

    public Trainee toTrainee(TraineeRequest request) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(request.firstName());
        trainee.setLastName(request.lastName());
        trainee.setAddress(request.address());
        trainee.setDateOfBirth(request.dateOfBirth());
        trainee.setUsername(trainee.getFirstName().concat(".").concat(trainee.getLastName()));
        trainee.setIsActive(true);
        return trainee;
    }

    public TraineeResponse toTraineeResponse(Trainee trainee) {
        List<TrainerResponse> trainerResponses = trainerMapper.toTrainerResponses(trainee.getTrainers());
        return new TraineeResponse(
                trainee.getId(),
                trainee.getFirstName(),
                trainee.getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getIsActive(),
                trainerResponses
        );
    }

    public List<TraineeResponse> toTraineeResponses(List<Trainee> trainees) {
        return trainees.stream()
                .map(this::toTraineeResponse)
                .toList();
    }

    public Trainee toUpdatedTrainee(Trainee trainee, TraineeRequest request) {
        trainee.setFirstName(request.firstName());
        trainee.setLastName(request.lastName());
        trainee.setAddress(request.address());
        trainee.setDateOfBirth(request.dateOfBirth());
        trainee.setIsActive(request.isActive());
        return trainee;
    }

}
