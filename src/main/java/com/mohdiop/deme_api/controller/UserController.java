package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.service.AuthenticationService;
import com.mohdiop.deme_api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/me")
    public ResponseEntity<Object> me() {
        return ResponseEntity.ok(userService.me(
                authenticationService.getCurrentUserId()
        ));
    }
}
