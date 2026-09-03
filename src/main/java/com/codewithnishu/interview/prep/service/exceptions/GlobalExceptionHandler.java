package com.codewithnishu.interview.prep.service.exceptions;

import com.codewithnishu.interview.prep.service.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError>  handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request){
       String errorMessage = ex.getBindingResult().getFieldErrors().stream()
               .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
               .collect(Collectors.joining(", "));
       ApiError apiError = new ApiError(
               400,
               "VALIDATION_FAILED",
               errorMessage,
               LocalDateTime.now(),
               request.getRequestURI()
       );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> genericExceptions(Exception exception, HttpServletRequest request){
        log.error("Unexpected error", exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body( new ApiError(500, "INTERNAL_SERVER_ERROR", "An unexpected error occurred. Please try again later.", LocalDateTime.now(), request.getRequestURI()));
    }
    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFound(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "status", 404,
                "error", "NOT_FOUND",
                "message", ex.getMessage()
        ));
    }

}
