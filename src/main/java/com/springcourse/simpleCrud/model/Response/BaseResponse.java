package com.springcourse.simpleCrud.model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResponse<T> {
    private String message;
    private String trace = "";
    private T data;

    public BaseResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.trace = ""; // Ensuring trace is always initialized
    }
}
