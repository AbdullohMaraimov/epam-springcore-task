package gym_crm.mapper;

import gym_crm.dto.request.TraineeRequest;
import gym_crm.dto.reponse.TraineeResponse;
import gym_crm.model.Trainee;
import gym_crm.util.PasswordGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
