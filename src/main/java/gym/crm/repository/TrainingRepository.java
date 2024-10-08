package gym.crm.repository;

import gym.crm.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    void deleteByTraineeUsername(String username);

    @Query("SELECT t FROM Training t JOIN t.trainee trainee JOIN t.trainer trainer " +
            "WHERE trainee.username = :username " +
            "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
            "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
            "AND (:trainerUsername IS NULL OR trainer.username = :trainerUsername) " +
            "AND (:trainingTypeId IS NULL OR t.trainingType.id = :trainingTypeId)")
    List<Training> findAllByCriteria(
            @Param("username") String username,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("trainerUsername") String trainerUsername,
            @Param("trainingTypeId") Long trainingTypeId
    );

    @Query("SELECT t FROM Training t JOIN t.trainer trainer JOIN t.trainee trainee " +
            "WHERE trainer.username = :username " +
            "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
            "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
            "AND (:traineeUsername IS NULL OR trainee.username = :traineeUsername)")
    List<Training> findAllByTrainerAndCategory(
            @Param("username") String username,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("traineeUsername") String traineeUsername
    );

}
