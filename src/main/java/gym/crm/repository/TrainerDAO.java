package gym.crm.repository;

import gym.crm.exception.CustomNotFoundException;
import gym.crm.model.Trainer;
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
public class TrainerDAO {

    public static int index = 1;

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public void save(Trainer trainer) {
        entityManager.persist(trainer);
    }

    @Transactional
    public void update(Trainer trainer) {
        entityManager.merge(trainer);
    }

    public Trainer findByUsername(String username) {
        String query = "from Trainer t left join fetch t.trainees where t.username = :username";
        TypedQuery<Trainer> typedQuery = entityManager.createQuery(query, Trainer.class);
        typedQuery.setParameter("username", username);

        try {
            return typedQuery.getSingleResult();
        } catch (Exception e) {
            throw new CustomNotFoundException("No Trainee found with username: %s".formatted(username));
        }
    }

    public List<Trainer> findAll() {
        List<Trainer> trainers = entityManager.createQuery("from Trainer", Trainer.class).getResultList();
        if (trainers.isEmpty()) {
            throw new CustomNotFoundException("No trainee found!");
        }
        return trainers;
    }

    public boolean isTrainerDBEmpty() {
        Long count = entityManager.createQuery(
                "select count(t) from Trainer t", Long.class)
                .getSingleResult();
        return count == 0;
    }

    public boolean isUsernameExists(String username) {
        Long count = entityManager.createQuery(
                "select count(t) from Trainer t where t.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }

    public Trainer findById(Long id) {
        try {
            return entityManager.createQuery(
                    "from Trainer t left join fetch t.trainees where t.id = :id", Trainer.class)
                    .setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            throw new CustomNotFoundException("Trainer with id %d not found!".formatted(id));
        }
    }

    public boolean isTrainerExistsWithId(Long id) {
        try {
            Long count = entityManager.createQuery("select count(t) from Trainer t where t.id = :id", Long.class)
                    .setParameter("id", id).getSingleResult();
            return count > 0;
        } catch (NoResultException e) {
            return false;
        }
    }

}
