package org.keycloak.usermanagement.domain.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {super( message );}
}
