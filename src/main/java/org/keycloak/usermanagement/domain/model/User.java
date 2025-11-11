package org.keycloak.usermanagement.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@AllArgsConstructor
public class User {
    private String id;
    private String username;
    private String email;
    private boolean isActive;
    private LocalDateTime createdAt;





}
