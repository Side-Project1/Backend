package com.project.server.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignUpRequest {
    private String userId;
    private String password;
    private String userName;
    private String phone;
    private String email;
    private String birthday;
    private String gender;
}
