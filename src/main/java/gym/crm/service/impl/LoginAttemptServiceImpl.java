package gym.crm.service.impl;

import gym.crm.model.LoginAttempt;
import gym.crm.repository.LoginAttemptRepository;
import gym.crm.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;
    private final int ATTEMPT_LIMIT = 3;

    @Override
    public void loginFailed(String username) {
        LoginAttempt loginAttempt = loginAttemptRepository.findByUsername(username)
                .orElse(new LoginAttempt(username, 0, null));

        loginAttempt.setAttempts(loginAttempt.getAttempts() + 1);

        if (loginAttempt.getAttempts() >= ATTEMPT_LIMIT) {
            loginAttempt.setLockTime(LocalDateTime.now().plusMinutes(5));
        }

        loginAttemptRepository.save(loginAttempt);
    }

    @Override
    public void loginSucceeded(String username) {
        loginAttemptRepository.deleteByUsername(username);
    }

    @Override
    public boolean isBlocked(String username) {
        LoginAttempt loginAttempt = loginAttemptRepository.findByUsername(username).orElse(null);

        if (loginAttempt != null && loginAttempt.getAttempts() >= ATTEMPT_LIMIT) {
            if (loginAttempt.getLockTime().isAfter(LocalDateTime.now())) {
                return true;
            } else {
                loginAttemptRepository.deleteByUsername(username);
            }
        }
        return false;
    }
}
