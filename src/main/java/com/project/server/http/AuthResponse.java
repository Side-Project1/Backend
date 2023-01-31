package com.project.server.http;

public class AuthResponse {
    private String accessToken;
    private String refreshToekn;
    private String tokenType = "Bearer";

    public AuthResponse(String accessToken, String refreshToekn) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
