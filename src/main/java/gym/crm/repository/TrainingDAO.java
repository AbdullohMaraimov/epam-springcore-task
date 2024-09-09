package gym.crm.repository;

import gym.crm.exception.CustomNotFoundException;
import gym.crm.model.Training;
import gym.crm.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TrainingDAO {

    private final EntityManager entityManager;

    public void save(Training training) {
        entityManager.persist(training);
    }

    public Training findById(Long id) {
        try {
            return entityManager.createQuery("from Training t where t.id = :id", Training.class)
                    .setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            throw new CustomNotFoundException("Training not found with id: %d".formatted(id));
        }
    }

    public List<Training> findAll() {
        List<Training> trainings = entityManager.createQuery("from Training", Training.class).getResultList();
        if (trainings.isEmpty()) {
            throw new CustomNotFoundException("No training found!");
        }
        return trainings;
    }

    public boolean isTrainingExist(Long id) {
        Long count = entityManager.createQuery(
                "select count(t) from Training t where t.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }

    public boolean isTrainingDBEmpty() {
        Long count = entityManager.createQuery(
                "select count(t) from Training t", Long.class)
                .getSingleResult();
        return count == 0;
    }

    public List<Training> findAllByCriteria(String username, LocalDate fromDate, LocalDate toDate, String trainerName, TrainingType trainingType) {
        String query = "FROM Training t JOIN t.trainee trainee JOIN t.trainer trainer " +
                "WHERE trainee.username = :username " +
                "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
                "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
                "AND (:trainerName IS NULL OR trainer.username = :trainerName) " +
                "AND (:trainingType IS NULL OR t.trainingType = :trainingType)";
        TypedQuery<Training> typedQuery = entityManager.createQuery(query, Training.class);

        typedQuery.setParameter("username", username);
        typedQuery.setParameter("toDate", toDate);
        typedQuery.setParameter("fromDate", fromDate);
        typedQuery.setParameter("trainerName", trainerName);
        typedQuery.setParameter("trainingType", trainingType);

        List<Training> trainings = typedQuery.getResultList();

        if (trainings.isEmpty()) {
            throw new CustomNotFoundException("No training found!");
        }
        return trainings;
    }

    public List<Training> findAllByTrainerAndCategory(String username, LocalDate fromDate, LocalDate toDate, String traineeName) {
        String query = "FROM Training t JOIN t.trainer trainer JOIN t.trainee trainee " +
                "WHERE trainer.username = :username " +
                "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
                "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
                "AND (:traineeName IS NULL OR trainee.username = :traineeName) ";

        TypedQuery<Training> typedQuery = entityManager.createQuery(query, Training.class);

        typedQuery.setParameter("username", username);
        typedQuery.setParameter("toDate", toDate);
        typedQuery.setParameter("fromDate", fromDate);
        typedQuery.setParameter("traineeName", traineeName);

        List<Training> trainings = typedQuery.getResultList();

        if (trainings.isEmpty()) {
            throw new CustomNotFoundException("No training found!");
        }
        return trainings;
    }

    public void update(Training training) {
        entityManager.merge(training);
    }

    public void deleteTrainingByTraineeUsername(String username) {
        entityManager.createQuery("delete from Training t where t.trainee.username = :username")
                .setParameter("username", username).executeUpdate();
    }
}
