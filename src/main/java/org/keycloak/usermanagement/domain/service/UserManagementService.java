package org.keycloak.usermanagement.domain.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.usermanagement.application.dto.request.LoginRequest;
import org.keycloak.usermanagement.application.dto.request.SignupRequest;
import org.keycloak.usermanagement.application.dto.response.UserResponse;
import org.keycloak.usermanagement.application.port.in.UserManagementUseCase;
import org.keycloak.usermanagement.infrastructure.adapter.out.KeycloakAdapter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserManagementService implements UserManagementUseCase {

    private final KeycloakAdapter keycloakAdapter;

    @Override
    public UserResponse signup(SignupRequest request) {
        String userId = keycloakAdapter.createUser(
                request.username(),
                request.email(),
                request.password()
        );

        return new UserResponse(userId, request.username(), request.email(), true);
    }

    @Override
    public UserResponse login(LoginRequest request) {

        String token = keycloakAdapter.login(request.email(), request.password());

        UserRepresentation user = keycloakAdapter.getUserByEmail(request.email());

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isEnabled()
        );
    }

    @Override
    public void deactivateAccount(String userId) {
        keycloakAdapter.deactivateUser(userId);
    }
}
