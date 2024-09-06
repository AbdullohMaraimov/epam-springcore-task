package gym.crm.repository;

import gym.crm.model.Trainee;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class TraineeDAO {

    public static int index = 1;
    private final Map<String, Trainee> traineeDB = new HashMap<>();

    private HibernateTemplate template;


    public void save(Trainee trainee) {
        template.save(trainee);
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
