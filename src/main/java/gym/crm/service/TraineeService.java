package gym.crm.service;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.RegistrationResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.reponse.TraineeResponse;

import java.util.List;

public interface TraineeService {

    RegistrationResponse create(TraineeRequest trainee);

    TraineeResponse update(String username, TraineeRequest trainee);

    void delete(String username);

    TraineeResponse findByUsername(String username);

    List<TraineeResponse> findAll();

    void deleteAll();

    void updatePassword(String username, String oldPassword, String newPassword);

    void deActivateUser(String username);

    void activateUser(String username);

    List<TrainerResponse> findAllUnassignedTrainers(String username);
}
