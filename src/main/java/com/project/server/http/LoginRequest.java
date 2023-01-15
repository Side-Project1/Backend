package com.project.server.http;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
