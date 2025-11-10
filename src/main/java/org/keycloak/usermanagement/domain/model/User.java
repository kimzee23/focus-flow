package org.keycloak.usermanagement.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
public class User {
    private String id;
    private String username;
    private String email;
    private boolean isActive;
    private LocalDateTime createdAt;

    public User(String id, String username, String email, boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }




}
