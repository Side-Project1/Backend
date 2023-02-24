package com.project.server.http.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> {
    private String message;
    private HttpStatus status;
    private T data;

    public ApiResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.data = null;
    }

    public ApiResponse(String message, HttpStatus status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }
}
