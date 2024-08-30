package gym.crm.repository;

import gym.crm.model.Training;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TrainingDAO {

    private final Map<String, Training> trainingDB = new HashMap<>();

    public void save(Training training) {
        trainingDB.put(training.getId(), training);
    }

    public Training findById(String id) {
        return trainingDB.get(id);
    }

    public List<Training> findAll() {
        return trainingDB.values().stream().toList();
    }

    public boolean isTrainingExist(String id) {
        return trainingDB.containsKey(id);
    }

    public boolean isTrainingDBEmpty() {
        return trainingDB.isEmpty();
    }

}
