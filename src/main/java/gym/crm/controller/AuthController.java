package gym.crm.controller;

import gym.crm.controller.documentation.AuthControllerDocumentation;
import gym.crm.dto.reponse.ApiResponse;
import gym.crm.dto.reponse.RegistrationResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.dto.request.TrainerRequest;
import gym.crm.dto.request.UserLoginRequest;
import gym.crm.metric.ApiCallService;
import gym.crm.metric.TimeMeasurementService;
import gym.crm.service.AuthService;
import gym.crm.service.JwtBlackListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocumentation {

    private final AuthService authService;
    private final ApiCallService apiCallService;
    private final TimeMeasurementService timeMeasurementService;
    private final JwtBlackListService jwtBlackListService;

    @PostMapping("/register-trainee")
    public ApiResponse<RegistrationResponse> register(@RequestBody @Valid TraineeRequest dto) throws Exception {
        log.info("Registering trainee with the request : {}", dto);
        RegistrationResponse registrationResponse = timeMeasurementService.measureTraineeRegistrationTime(() -> authService.register(dto));
        return new ApiResponse<>(201, true, registrationResponse, "Saved successfully!");
    }

    @PostMapping("/register-trainer")
    public ApiResponse<RegistrationResponse> register(@RequestBody @Valid TrainerRequest dto) throws Exception {
        log.info("Registering trainer with the request : {}", dto);
        RegistrationResponse registrationResponse = timeMeasurementService.measureTrainerRegistrationTime(() -> authService.register(dto));
        return new ApiResponse<>(201, true,   registrationResponse, "Saved successfully!");
    }

    @GetMapping("/login")
    public ApiResponse<String> login(@Valid @RequestBody UserLoginRequest dto) {
        log.info("Logging in with username : {}", dto.username());
        String login = authService.login(dto);
        apiCallService.trackLogin();
        return new ApiResponse<>(200, true, login, "OK");
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Logging out with username : {}", username);
        String extractedJwt = extractJwt(request);
        if (extractedJwt != null && jwtBlackListService.blackListToken(extractedJwt)) {
            return new ApiResponse<>(201, "Logout Successful", true);
        }
        return new ApiResponse<>(400, "Bad request", false);
    }

    private String extractJwt(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");
        if (StringUtils.hasText(jwt) && jwt.startsWith("Bearer ")) {
            return jwt.substring(7);
        }
        return null;
    }
}
