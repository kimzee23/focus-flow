package org.keycloak.usermanagement.application.port.in;

import org.keycloak.usermanagement.application.dto.request.LoginRequest;
import org.keycloak.usermanagement.application.dto.request.SignupRequest;
import org.keycloak.usermanagement.application.dto.response.UserResponse;

public interface UserManagementUseCase {
    UserResponse signup(SignupRequest request);
    UserResponse login(LoginRequest request);
    void deactivateAccount(String userId);
    void forgotPassword(String email);
    void resetPassword(String userId, String newPassword);
}