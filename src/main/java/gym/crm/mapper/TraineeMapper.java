package gym.crm.mapper;

import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.reponse.TraineeResponse;
import gym.crm.model.Trainee;
import gym.crm.util.PasswordGenerator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TraineeMapper {

    public Trainee toTrainee(TraineeRequest request) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(request.firstName());
        trainee.setLastName(request.lastName());
        trainee.setAddress(request.address());
        trainee.setPassword(PasswordGenerator.generatePassword());
        trainee.setDateOfBirth(request.dateOfBirth());
        trainee.setUsername(trainee.getFirstName().concat(".").concat(trainee.getLastName()));
        trainee.setIsActive(true);
        return trainee;
    }


    public TraineeResponse toTraineeResponse(Trainee trainee) {
        return new TraineeResponse(
                trainee.getUserId().toString(),
                trainee.getFirstName(),
                trainee.getLastName(),
                trainee.getUsername(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getIsActive()
        );
    }


    public List<TraineeResponse> toTraineeResponses(List<Trainee> trainees) {
        List<TraineeResponse> traineeResponses = new ArrayList<>();
        for (Trainee trainee : trainees) {
            traineeResponses.add(toTraineeResponse(trainee));
        }
        return traineeResponses;
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
