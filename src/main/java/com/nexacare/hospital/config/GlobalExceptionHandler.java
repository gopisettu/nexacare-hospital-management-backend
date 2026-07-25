package com.nexacare.hospital.config;

import com.nexacare.hospital.dto.response.ErrorMessageDto;
import com.nexacare.hospital.exception.*;
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

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorMessageDto>handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e){
return  ResponseEntity
        .badRequest()
        .body(new ErrorMessageDto("UserName Already Exists !"));
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e){
        return  ResponseEntity
                .badRequest()
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

        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<ErrorMessageDto> handleUnauthorizedOperationException(
            UnauthorizedOperationException ex) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessageDto(ex.getMessage()));
    }
    @ExceptionHandler(InvalidAppointmentStateException.class)
    public ResponseEntity<ErrorMessageDto>handleInvalidAppointmentStateException(InvalidAppointmentStateException e){
        return  ResponseEntity
                .badRequest()
                .body(new ErrorMessageDto(e.getMessage()));
    }
    @ExceptionHandler(DoctorAlreadyBookedException.class)
    public ResponseEntity<ErrorMessageDto>handleDoctorAlreadyBookedException(DoctorAlreadyBookedException e){
        return  ResponseEntity
                .badRequest()
                .body(new ErrorMessageDto(e.getMessage()));
    }

    @ExceptionHandler(IllegalOperationException.class)
    public ResponseEntity<ErrorMessageDto> handleIllegalOperationException(IllegalOperationException e){
        return  ResponseEntity
                .badRequest()
                .body(new ErrorMessageDto(e.getMessage()));
    }

}
