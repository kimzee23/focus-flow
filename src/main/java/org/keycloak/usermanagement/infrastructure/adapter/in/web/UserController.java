package org.keycloak.usermanagement.infrastructure.adapter.in.web;

import lombok.AllArgsConstructor;
import org.keycloak.usermanagement.application.dto.request.LoginRequest;
import org.keycloak.usermanagement.application.dto.request.SignupRequest;
import org.keycloak.usermanagement.application.dto.response.UserResponse;
import org.keycloak.usermanagement.application.port.in.UserManagementUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("api/user")
@AllArgsConstructor
@RestController
public class UserController {

    private final UserManagementUseCase userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody SignupRequest request) {
        UserResponse user = userService.signup(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        UserResponse user = userService.login(request);
        return ResponseEntity.ok(user);

    }
    @PostMapping("/deactivate/{userId}")
    public ResponseEntity<Void> deactivate(@PathVariable String userId) {
        userService.deactivateAccount(userId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotpassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }
}
