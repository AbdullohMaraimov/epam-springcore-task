package gym.crm.exception.handler;

import gym.crm.dto.reponse.ApiResponse;
import gym.crm.exception.CustomNotFoundException;
import jakarta.persistence.NoResultException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomNotFoundException.class)
    public ApiResponse<Void> handleCustomNotFoundException(CustomNotFoundException ex) {
        return new ApiResponse<>(
                404,
                false,
                null,
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ApiResponse<>(
                400,
                false,
                null,
                ex.getMessage()
        );
    }

    @ExceptionHandler(NoResultException.class)
    public ApiResponse<Void> handleNoResultException(NoResultException ex) {
        return new ApiResponse<>(
                400,
                false,
                null,
                ex.getMessage()
        );
    }

}
