package gym.crm.repository;

import gym.crm.exception.CustomNotFoundException;
import gym.crm.model.Trainee;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TraineeDAO {

    @PersistenceContext
    private final EntityManager entityManager;

    public static int index = 1;

    @Transactional
    public void save(Trainee trainee) {
        entityManager.persist(trainee);
    }

    @Transactional
    public void update(Trainee trainee) {
        entityManager.merge(trainee);
    }

    public void delete(String username) {
        String query = "delete from Trainee t where t.username = :username";
        entityManager.createQuery(query)
                .setParameter("username", username)
                .executeUpdate();
    }

    public void deleteAll() {
        entityManager.createQuery("delete from Trainee").executeUpdate();
    }

    public Trainee findByUsername(String username) {
        String query = "from Trainee t left join fetch t.trainers WHERE t.username = :username";
        TypedQuery<Trainee> typedQuery = entityManager.createQuery(query, Trainee.class);
        typedQuery.setParameter("username", username);

        try {
            return typedQuery.getSingleResult();
        } catch (Exception e) {
            throw new CustomNotFoundException("No Trainee found with username: %s".formatted(username));
        }
    }

    public List<Trainee> findAll() {
        List<Trainee> trainees = entityManager.createQuery("from Trainee", Trainee.class).getResultList();
        if (trainees.isEmpty()) {
            throw new CustomNotFoundException("No trainee found!");
        }
        return trainees;
    }

    public boolean isUsernameExists(String username) {
        Long count = entityManager.createQuery(
                "select count(t) from Trainee t where t.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }

    public boolean isTraineeDBEmpty() {
        Long count = entityManager.createQuery(
                "select count(t) from Trainee t", Long.class)
                .getSingleResult();
        return count == 0;
    }

    public Trainee findById(Long id) {
        try {
            return entityManager.createQuery(
                    "from Trainee t left join fetch t.trainers where t.id = :id", Trainee.class)
                    .setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            throw new CustomNotFoundException("Trainee not found with id %d".formatted(id));
        }
    }

    public boolean isTraineeExistsWithId(Long id) {
        Long count = entityManager.createQuery("select count(t) from Trainee t where t.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }

    public void deleteTraineeByUsername(String username) {
        entityManager.createQuery("delete from Trainee t where t.username = :username")
                .setParameter("username", username).executeUpdate();
    }

}
