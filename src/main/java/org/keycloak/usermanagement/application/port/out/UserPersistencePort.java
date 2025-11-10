package org.keycloak.usermanagement.application.port.out;

import org.keycloak.usermanagement.domain.model.User;

public interface UserPersistencePort {
    User save(User user);
    User findById(String id);
    void delete(String id);
}