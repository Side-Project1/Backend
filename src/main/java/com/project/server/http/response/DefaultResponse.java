package com.project.server.http.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class DefaultResponse<T> {
    private String message;
    private int status;
    private T data;

    public DefaultResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value();
        this.data = null;
    }

    public DefaultResponse(String message, HttpStatus status, T data) {
        this.message = message;
        this.status = status.value();
        this.data = data;
    }
}
