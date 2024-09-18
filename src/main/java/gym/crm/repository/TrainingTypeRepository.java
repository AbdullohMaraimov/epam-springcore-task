package gym.crm.repository;

import gym.crm.exception.CustomNotFoundException;
import gym.crm.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TrainingTypeRepository {

    private final EntityManager entityManager;

    @Transactional
    public void save(String trainingTypeName) {
        entityManager.persist(new TrainingType(trainingTypeName));
    }

    public TrainingType findById(Long id) {
        try {
            return entityManager.createQuery("from TrainingType t where t.id = :id", TrainingType.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new CustomNotFoundException("Training type with id : %d not found".formatted(id));
        }
    }

    public TrainingType findByTrainingName(String specializationName) {
        try {
            return entityManager.createQuery("from TrainingType t where t.name = :name", TrainingType.class)
                    .setParameter("name", specializationName)
                    .getSingleResult();
        } catch (NoResultException e) {
            log.error("Training type with name : %s not found".formatted(specializationName));
            return null;
        }
    }

    public List<TrainingType> findAll() {
        List<TrainingType> trainingTypes = entityManager.createQuery("from TrainingType", TrainingType.class)
                .getResultList();
        if (trainingTypes.isEmpty()) {
            throw new CustomNotFoundException("No training types are found");
        }
        return trainingTypes;
    }
}
