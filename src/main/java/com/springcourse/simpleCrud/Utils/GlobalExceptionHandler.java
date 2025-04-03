package com.springcourse.simpleCrud.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springcourse.simpleCrud.model.Response.BaseResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class) // Handles all exceptions
    public ResponseEntity<BaseResponse<String>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseResponse<>("Internal server error!", ex.getMessage(), null));
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new BaseResponse<>("Authentication error!", ex.getMessage(), null));
    }
}
