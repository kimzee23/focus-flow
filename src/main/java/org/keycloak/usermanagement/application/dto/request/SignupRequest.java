package org.keycloak.usermanagement.application.dto.request;

public record SignupRequest(String username , String email, String password) {
}
