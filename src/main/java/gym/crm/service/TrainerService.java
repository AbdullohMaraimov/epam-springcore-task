package gym.crm.service;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.RegistrationResponse;
import gym.crm.dto.reponse.TrainerResponse;
import gym.crm.dto.request.TrainerRequest;

import java.util.List;

public interface TrainerService {

    RegistrationResponse create(TrainerRequest trainer);

    TrainerResponse update(String username, TrainerRequest trainer);

    TrainerResponse findByUsername(String username);

    List<TrainerResponse> findAll();

    void updatePassword(String username, String oldPassword, String newPassword);

    void deActivateUser(String username);

    void activateUser(String username);
}
