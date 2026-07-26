package com.nexacare.hospital.config;

import com.nexacare.hospital.dto.response.ErrorMessageDto;
import com.nexacare.hospital.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice


public class GlobalExceptionHandler {
    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorMessageDto>handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e){

        logger.error("SQL Integrity Constraint Violation: {}", e.getMessage(), e);
        return  ResponseEntity
        .badRequest()

        .body(new ErrorMessageDto("UserName Already Exists !"));
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e){

        logger.warn("Resource not found: {}", e.getMessage());

        return  ResponseEntity

                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(
            MethodArgumentNotValidException ex){

        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(err ->
                        errors.put(err.getField(), err.getDefaultMessage()));
        logger.warn("Validation failed: {}", errors);

        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<ErrorMessageDto> handleUnauthorizedOperationException(
            UnauthorizedOperationException ex) {
        logger.warn("Unauthorized operation: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessageDto(ex.getMessage()));
    }
    @ExceptionHandler(InvalidAppointmentStateException.class)
    public ResponseEntity<ErrorMessageDto>handleInvalidAppointmentStateException(InvalidAppointmentStateException e){
        logger.warn("Invalid appointment state: {}", e.getMessage());
        return  ResponseEntity
                .badRequest()
                .body(new ErrorMessageDto(e.getMessage()));
    }
    @ExceptionHandler(DoctorAlreadyBookedException.class)
    public ResponseEntity<ErrorMessageDto>handleDoctorAlreadyBookedException(DoctorAlreadyBookedException e){
        logger.warn("Doctor already booked: {}", e.getMessage());
        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessageDto(e.getMessage()));
    }

    @ExceptionHandler(IllegalOperationException.class)
    public ResponseEntity<ErrorMessageDto> handleIllegalOperationException(IllegalOperationException e){

        logger.warn("Illegal operation: {}", e.getMessage());
        return  ResponseEntity
                .status(HttpStatus.BAD_REQUEST)

                .body(new ErrorMessageDto(e.getMessage()));
    }

}
