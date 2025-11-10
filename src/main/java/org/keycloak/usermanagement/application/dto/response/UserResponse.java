package org.keycloak.usermanagement.application.dto.response;

public record UserResponse(String id, String username, String email, boolean active) {}
