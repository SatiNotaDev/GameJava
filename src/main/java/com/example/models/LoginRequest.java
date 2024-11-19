package com.example.models;

// LoginRequest.java
public class LoginRequest {
    private String nickname;
    
    // Constructor
    public LoginRequest() {}
    
    public LoginRequest(String nickname) {
        this.nickname = nickname;
    }
    
    // Getters and setters
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}