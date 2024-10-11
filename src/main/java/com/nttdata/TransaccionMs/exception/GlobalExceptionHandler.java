package com.nttdata.TransaccionMs.exception;

import com.nttdata.TransaccionMs.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomExceptions.ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(CustomExceptions.ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCodigo(HttpStatus.NOT_FOUND.value());
        errorResponse.setMensaje("Recurso no encontrado");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomExceptions.BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(CustomExceptions.BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCodigo(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMensaje("Solicitud inválida");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

