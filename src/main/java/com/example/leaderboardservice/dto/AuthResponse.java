package com.example.leaderboardservice.dto;

public class AuthResponse {
    private boolean success;
    private String message;
    private String token; // NEW

    public AuthResponse() {}

    // old 2-arg constructor
    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // new 3-arg constructor
    public AuthResponse(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
