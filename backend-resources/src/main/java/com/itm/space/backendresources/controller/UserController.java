package com.itm.space.backendresources.controller;

import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @Secured("ROLE_MODERATOR")
    @SecurityRequirement(name = "oauth2_auth_code")
    public void create(@RequestBody @Valid UserRequest userRequest) {
        userService.createUser(userRequest);
    }

    @GetMapping("/{id}")
    @Secured("ROLE_MODERATOR")
    @SecurityRequirement(name = "oauth2_auth_code")
    public UserResponse getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @GetMapping("/hello")
    @Secured("ROLE_MODERATOR")
    @SecurityRequirement(name = "oauth2_auth_code")
    public String hello() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}