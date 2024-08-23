package gym_crm.repository;

import gym_crm.model.Training;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
