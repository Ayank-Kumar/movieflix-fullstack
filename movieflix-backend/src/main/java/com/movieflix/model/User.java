package com.movieflix.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotBlank
    @Size(min = 3, max = 50)
    @Indexed(unique = true)
    private String username;

    @Email
    @Indexed(unique = true)
    private String email;

    @NotBlank
    private String password;

    private List<String> roles;

    private boolean isActive = true;

    // Now using java.util.Date for timestamps
    private Date createdAt;
    private Date lastLoginAt;

    // Constructors
    public User() {
        this.createdAt = new Date();
    }

    public User(String username, String email, String password, List<String> roles) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(Date lastLoginAt) { this.lastLoginAt = lastLoginAt; }

    // Helper to update last login timestamp
    public void updateLastLogin() {
        this.lastLoginAt = new Date();
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", isActive=" + isActive +
                '}';
    }
}
