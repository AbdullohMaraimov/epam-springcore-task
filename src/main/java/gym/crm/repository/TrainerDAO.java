package gym.crm.repository;

import gym.crm.model.Trainer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TrainerDAO {

    public static int index = 0;

    private final Map<String, Trainer> trainerDB = new HashMap<>();

    public void save(Trainer trainer) {
        trainerDB.put(trainer.getUsername(), trainer);
    }

    public void update(Trainer trainer) {
        trainerDB.put(trainer.getUsername(), trainer);
    }

    public Trainer findByUsername(String username) {
        return trainerDB.get(username);
    }

    public List<Trainer> findAll() {
        return trainerDB.values().stream().toList();
    }

    public boolean isTrainerDBEmpty() {
        return trainerDB.isEmpty();
    }

    public boolean isUsernameExists(String username) {
        return trainerDB.containsKey(username);
    }
}
