package com.nexacare.hospital.config;

import com.nexacare.hospital.dto.response.errorres.ErrorMessageDto;
import com.nexacare.hospital.exception.*;
import com.nexacare.hospital.exception.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

@ControllerAdvice


public class GlobalExceptionHandler {
    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final Map<String, String> SQL_ERROR_MESSAGES = Map.ofEntries(
            Map.entry("username", "Username already exists."),
            Map.entry("password", "Password cannot be empty."),
            Map.entry("role", "Role cannot be empty."),
            Map.entry("firstname", "First name cannot be empty."),
            Map.entry("lastname", "Last name cannot be empty."),
            Map.entry("gender", "Please select gender."),
            Map.entry("dob", "Date of birth cannot be empty."),
            Map.entry("aadharnumber", "Aadhar number already exists."),
            Map.entry("bloodgroup", "Please select blood group."),
            Map.entry("phone", "Phone number already exists."),
            Map.entry("email", "Email already exists."),
            Map.entry("address", "Address cannot be empty."),
            Map.entry("allergies", "Allergies cannot be empty."),
            Map.entry("chronicdisease", "Chronic disease cannot be empty."),
            Map.entry("createdat", "Created date cannot be empty.")
    );


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorMessageDto> handleSQLIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException e) {

        logger.error("SQL Integrity Constraint Violation: {}", e.getMessage(), e);

        String error = e.getMessage().toLowerCase();

        String message = SQL_ERROR_MESSAGES.entrySet()
                .stream()
                .filter(entry -> error.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageDto(message));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e){

        logger.warn("Resource not found: {}", e.getMessage());

        return  ResponseEntity

                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDto> handleValidation(
            MethodArgumentNotValidException ex){

        String message = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        logger.warn("Validation failed: {}", message);

        return ResponseEntity
                .badRequest()
                .body(new ErrorMessageDto(message));
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


    public  ResponseEntity<ErrorMessageDto> handleFileUploadException(FileUploadException e){
        return  ResponseEntity
                .badRequest()
                .body(new ErrorMessageDto(e.getMessage()));
    }
}
