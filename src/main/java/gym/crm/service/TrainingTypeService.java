package gym.crm.service;

import gym.crm.dto.request.TrainingTypeRequest;
import gym.crm.model.TrainingType;

public interface TrainingTypeService {

    TrainingType findByName(String name);

    void createTrainingType(TrainingTypeRequest trainingTypeRequest);

}
