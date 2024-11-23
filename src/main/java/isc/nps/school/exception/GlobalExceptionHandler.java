package isc.nps.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import isc.nps.school.constant.ErrorConstant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        // Customize error response
        String errorMessage = e.getMessage() != null ? e.getMessage() : "An unexpected error occurred";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // Default error status

        // Handling different exceptions and setting appropriate status
        if (e instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (e instanceof NullPointerException) {
            status = HttpStatus.NOT_FOUND;
        }

        // You can customize the error response body
        ErrorResponse errorResponse = new  ErrorResponse(status.value(), errorMessage);
        return new ResponseEntity<>(errorResponse, status);
    }
}