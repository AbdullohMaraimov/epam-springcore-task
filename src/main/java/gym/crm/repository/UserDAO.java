package gym.crm.repository;

import gym.crm.exception.CustomNotFoundException;
import gym.crm.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDAO {

    @PersistenceContext
    private final EntityManager entityManager;

    public User findByUsername(String username) {
        try {
            log.info("Searching with username %s".formatted(username));
            User user = entityManager.createQuery("from User u where u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            log.info("User found: " + user);
            return user;
        } catch (NoResultException e) {
            throw new CustomNotFoundException("User not found with username : %s".formatted(username));
        }
    }

}
