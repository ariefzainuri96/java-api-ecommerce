package com.springcourse.simpleCrud.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springcourse.simpleCrud.model.Response.BaseResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class) // Handles all exceptions
    public ResponseEntity<BaseResponse<String>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseResponse<>("Internal server error!", ex.getMessage(), null));
    }

    // Handles custom exceptions here
    // Create class ProductNotFoundExceptions that extends RuntimeException
    // @ExceptionHandler(ProductNotFoundException.class) // Custom exception
    // public ResponseEntity<BaseResponse<String>>
    // handleProductNotFound(ProductNotFoundException ex) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND)
    // .body(new BaseResponse<>(false, "Product not found", ex.getMessage()));
    // }
}
