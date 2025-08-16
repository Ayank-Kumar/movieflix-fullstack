package com.movieflix.dto.response;

import java.util.List;

public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String username;
    private List<String> roles;
    private long expiresIn;

    // Constructors
    public JwtResponse() {}

    public JwtResponse(String token, String username, List<String> roles, long expiresIn) {
        this.token = token;
        this.username = username;
        this.roles = roles;
        this.expiresIn = expiresIn;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
}
