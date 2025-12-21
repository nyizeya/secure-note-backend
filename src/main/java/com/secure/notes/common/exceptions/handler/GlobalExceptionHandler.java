package com.secure.notes.common.exceptions.handler;

import com.secure.notes.common.exceptions.InvalidOperationException;
import com.secure.notes.web.dtos.MessageResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<MessageResponseDTO> handleInvalidOperationException(InvalidOperationException e) {
        return ResponseEntity.badRequest().body(new MessageResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorList = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> errorList.add(error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(new MessageResponseDTO(errorList.get(0)));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<MessageResponseDTO> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(new MessageResponseDTO(e.getMessage()));
    }

}
