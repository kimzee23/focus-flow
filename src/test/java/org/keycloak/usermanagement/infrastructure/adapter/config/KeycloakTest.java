package org.keycloak.usermanagement.infrastructure.adapter.config;

import static org.junit.jupiter.api.Assertions.*;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public class KeycloakTest {
    public static void main(String[] args) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8090")
                .realm("master")
                .username("admin")
                .password("admin")
                .clientId("admin-cli")
                .build();

        try {
            List<UserRepresentation> users = keycloak.realm("user-management").users().list();
            System.out.println("Users in user-management realm:");
            for (UserRepresentation u : users) {
                System.out.println(u.getUsername() + " | " + u.getEmail() + " | " + u.isEnabled());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            keycloak.close();
        }
    }
}
