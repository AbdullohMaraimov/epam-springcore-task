package gym.crm.exception.handler;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.exception.CustomNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    public ApiResponse<Void> handleCustomNotFoundException(CustomNotFoundException ex) {
        return new ApiResponse<>(
                404,
                false,
                null,
                ex.getMessage()
        );
    }

}
