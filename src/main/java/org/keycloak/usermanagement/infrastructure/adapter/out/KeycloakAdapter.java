package org.keycloak.usermanagement.infrastructure.adapter.out;


import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.usermanagement.domain.exception.UserAlreadyExistsException;
import org.keycloak.usermanagement.domain.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeycloakAdapter {

    private final Keycloak keycloak;
    private final String realmName;

    public KeycloakAdapter(Keycloak keycloak, @Value("${keycloak.user-realm:user-management}") String realmName) {
        this.keycloak = keycloak;
        this.realmName = realmName;
    }

    public String createUser(String username, String email, String password) {
        List<UserRepresentation> existingUsers = keycloak.realm(realmName)
                .users()
                .search(null, null, null, email, 0, 1);

        if (existingUsers != null && !existingUsers.isEmpty()) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists");
        }

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);
        user.setEmailVerified(false);

        keycloak.realm(realmName).users().create(user);

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(password);
        cred.setTemporary(false);

        List<UserRepresentation> users = keycloak.realm(realmName).users().search(username, true);
        if (users == null || users.isEmpty()) {
            throw new RuntimeException("User creation failed");
        }

        String userId = users.get(0).getId();

        keycloak.realm(realmName).users().get(userId).resetPassword(cred);

        return userId;
    }


    public void deactivateUser(String userId) {
        UserRepresentation user = keycloak.realm(realmName).users().get(userId).toRepresentation();
        user.setEnabled(false);
        keycloak.realm(realmName).users().get(userId).update(user);
    }
    public UserRepresentation getUserByEmail(String email) {
        try {
            List<UserRepresentation> users = keycloak.realm(realmName)
                    .users()
                    .search(null, null, null, email, 0, 1);
            if (users == null || users.isEmpty()) {
                throw new UserNotFoundException("User not found");
            }
            return users.get(0);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user: " + e.getMessage(), e);
        }
    }


    public String login(String username, String password) {
        return "login-token-placeholder";
    }

    public void resetPassword(String userId, String newPassword) {
        RealmResource realm = keycloak.realm(realmName);
        UsersResource users = realm.users();
        UserResource userResource = users.get(userId);

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(newPassword);
        cred.setTemporary(false);

        userResource.resetPassword(cred);
    }


    public void triggerResetPassword(String email) {
        List<UserRepresentation> users = keycloak.realm(realmName).users().search(email);
        if (users.isEmpty()) throw new UserNotFoundException("User not found");

        String userId = users.get(0).getId();
        keycloak.realm(realmName).users().get(userId)
                .executeActionsEmail(List.of("UPDATE_PASSWORD"));
    }
    public void triggerEmailVerification(String userId) {
        try {
            keycloak.realm(realmName).users().get(userId).sendVerifyEmail();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Skipping email verification: " + e.getMessage());
        }
    }

}