package gym_crm.repository;

import gym_crm.model.Trainee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraineeDAO {

    public static int index = 1;

    private final Map<String, Trainee> traineeDB = new HashMap<>();

    public void save(Trainee trainee) {
        traineeDB.put(trainee.getUsername(), trainee);
    }

    public void update(Trainee trainee) {
        traineeDB.put(trainee.getUsername(), trainee);
    }

    public void delete(String username) {
        traineeDB.remove(username);
    }

    public void deleteAll() {
        traineeDB.clear();
    }

    public Trainee findByUsername(String username) {
        return traineeDB.get(username);
    }

    public List<Trainee> findAll() {
        return traineeDB.values().stream().toList();
    }

    public boolean isUsernameExists(String username) {
        return traineeDB.containsKey(username);
    }

    public boolean isTraineeDBEmpty() {
        return traineeDB.isEmpty();
    }

}
