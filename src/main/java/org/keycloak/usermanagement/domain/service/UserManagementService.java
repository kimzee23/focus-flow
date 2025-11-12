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

        keycloakAdapter.triggerEmailVerification(userId);

        return new UserResponse(userId, request.username(), request.email(), true);
    }

    @Override
    public UserResponse login(LoginRequest request) {
        UserRepresentation user = keycloakAdapter.getUserByEmail(request.email());

        if (!user.isEmailVerified()){
            throw new IllegalStateException("Please verify your email");
        }
        String token = keycloakAdapter.login(request.email(), request.password());

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
    @Override
    public void activateAccount(String userId){
        keycloakAdapter.activateUser(userId);
    }

    @Override
    public void forgotPassword(String email) {
        keycloakAdapter.triggerResetPassword(email);
    }

    @Override
    public void resetPassword(String userId, String newPassword) {
        keycloakAdapter.resetPassword(userId, newPassword);

    }

    @Override
    public void triggerEmailVerification(String userId) {
        keycloakAdapter.triggerEmailVerification(userId);
    }
}
