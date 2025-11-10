package org.keycloak.usermanagement.infrastructure.adapter.out;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeycloakAdapter {

    private final Keycloak keycloak;

    public KeycloakAdapter(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public String createUser(String username, String email, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);

        keycloak.realm("user-management").users().create(user);

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(password);
        cred.setTemporary(false);

        List<UserRepresentation> users = keycloak.realm("user-management").users().search(username);
        if (users.isEmpty()) {
            throw new RuntimeException("User creation failed");
        }
        String userId = users.get(0).getId();
        keycloak.realm("user-management").users().get(userId).resetPassword(cred);

        return userId;
    }

    public void deactivateUser(String userId) {
        UserRepresentation user = keycloak.realm("user-management").users().get(userId).toRepresentation();
        user.setEnabled(false);
        keycloak.realm("user-management").users().get(userId).update(user);
    }
    public String login(String username, String password) {
        return "login-token-placeholder";
    }
}
