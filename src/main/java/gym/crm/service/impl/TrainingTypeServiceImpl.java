package gym.crm.service.impl;

import gym.crm.dto.request.TrainingTypeRequest;
import gym.crm.exception.CustomNotFoundException;
import gym.crm.model.TrainingType;
import gym.crm.repository.TrainingTypeRepository;
import gym.crm.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public TrainingType findByName(String name) {
        return trainingTypeRepository.findByName(name)
                .orElseThrow(() -> new CustomNotFoundException("Training type with name %s not found"));
    }

    @Override
    public void createTrainingType(TrainingTypeRequest trainingTypeRequest) {
        trainingTypeRepository.save(new TrainingType(trainingTypeRequest.name()));
    }
}
