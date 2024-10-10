package gym.crm.repository;

import gym.crm.model.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, String> {

    Optional<LoginAttempt> findByUsername(String username);

    void deleteByUsername(String username);
}
