package org.keycloak.usermanagement.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.usermanagement.application.dto.request.LoginRequest;
import org.keycloak.usermanagement.application.dto.request.SignupRequest;
import org.keycloak.usermanagement.application.dto.response.UserResponse;
import org.keycloak.usermanagement.infrastructure.adapter.out.KeycloakAdapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserManagementServiceTest {

    private KeycloakAdapter keycloakAdapter;
    private UserManagementService userService;

    @BeforeEach
    void setup() {
        keycloakAdapter = mock(KeycloakAdapter.class);
        userService = new UserManagementService(keycloakAdapter);
    }

    @Test
    void testSignup() {
        SignupRequest request = new SignupRequest("ade", "ade@gmail.com", "password123");
        when(keycloakAdapter.createUser(request.username(), request.email(), request.password()))
                .thenReturn("user-id-1234");

        var response = userService.signup(request);

        assertEquals("user-id-1234", response.id());
        assertEquals("ade", response.username());
        assertEquals("ade@gmail.com", response.email());
        assertTrue(response.active());
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest("ade@gmail.com", "password123");
        UserRepresentation mockUser = new UserRepresentation();
        mockUser.setId("user-id-1234");
        mockUser.setUsername("ade");
        mockUser.setEmail("ade@gmail.com");
        mockUser.setEnabled(true);

        when(keycloakAdapter.login(request.email(), request.password())).thenReturn("token");
        when(keycloakAdapter.getUserByEmail(request.email())).thenReturn(mockUser);

        UserResponse response = userService.login(request);

        assertEquals("user-id-1234", response.id());
        assertEquals("ade", response.username());
        assertEquals("ade@gmail.com", response.email());
        assertTrue(response.active());
    }

    @Test
    void testDeactivate() {
        doNothing().when(keycloakAdapter).deactivateUser("user-id-1234");
        userService.deactivateAccount("user-id-1234");
        verify(keycloakAdapter, times(1)).deactivateUser("user-id-1234");
    }

    @Test
    void testForgotPassword() {
        String email = "ade@gmail.com";
        doNothing().when(keycloakAdapter).triggerResetPassword(email);
        userService.forgotPassword(email);
        verify(keycloakAdapter, times(1)).triggerResetPassword(email);
    }

    @Test
    void testResetPassword() {
        doNothing().when(keycloakAdapter).resetPassword("user-id-1234", "newPassword");
        userService.resetPassword("user-id-1234", "newPassword");
        verify(keycloakAdapter, times(1)).resetPassword("user-id-1234", "newPassword");
    }
}