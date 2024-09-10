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

    public List<Training> findAllByCriteria(String username, LocalDate fromDate, LocalDate toDate, String trainerUsername, Long trainingTypeId) {
        String query = "from Training t join t.trainee trainee join t.trainer trainer " +
                "where trainee.username = :username " +
                "and (cast(:fromDate as date) is null or t.trainingDate >= :fromDate) " +
                "and (cast(:toDate as date) is null or t.trainingDate <= :toDate) " +
                "and (:trainerUsername is null or trainer.username = :trainerUsername) " +
                "and (:trainingTypeId is null or t.trainingType.id = :trainingTypeId)";
        TypedQuery<Training> typedQuery = entityManager.createQuery(query, Training.class);

        typedQuery.setParameter("username", username);
        typedQuery.setParameter("toDate", toDate);
        typedQuery.setParameter("fromDate", fromDate);
        typedQuery.setParameter("trainerUsername", trainerUsername);
        typedQuery.setParameter("trainingTypeId", trainingTypeId);

        List<Training> trainings = typedQuery.getResultList();

        if (trainings.isEmpty()) {
            throw new CustomNotFoundException("No training found!");
        }
        return trainings;
    }

    public List<Training> findAllByTrainerAndCategory(String username, LocalDate fromDate, LocalDate toDate, String traineeUsername) {
        String query = "from Training t join t.trainer trainer join t.trainee trainee " +
                "where trainer.username = :username " +
                "and (cast(:fromDate as date) is null or t.trainingDate >= :fromDate) " +
                "and (cast(:toDate as date) is null or t.trainingDate <= :toDate) " +
                "and (:traineeUsername is null or trainee.username = :traineeUsername) ";

        TypedQuery<Training> typedQuery = entityManager.createQuery(query, Training.class);

        typedQuery.setParameter("username", username);
        typedQuery.setParameter("toDate", toDate);
        typedQuery.setParameter("fromDate", fromDate);
        typedQuery.setParameter("traineeUsername", traineeUsername);

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
