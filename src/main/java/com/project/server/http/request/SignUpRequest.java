package com.project.server.http.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String password;
    private String name;
}
