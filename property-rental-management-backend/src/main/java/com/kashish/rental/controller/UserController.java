package com.kashish.rental.controller;

import java.util.List;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import com.kashish.rental.dto.LoginResponse;
import org.springframework.web.bind.annotation.*;
import com.kashish.rental.dto.LoginRequest;
import com.kashish.rental.dto.UserResponse;
import com.kashish.rental.entity.User;
import com.kashish.rental.service.UserService;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse addUser(@Valid @RequestBody User user) {

        User savedUser = service.addUser(user);

        return convertToResponse(savedUser);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {

        return service.getAllUsers()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {

        User user = service.getUserById(id);

        return convertToResponse(user);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        return service.login(
                request.getEmail(),
                request.getPassword()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable Long id,
            Authentication authentication) {

        service.deleteUser(
                id,
                authentication.getName()
        );
    }

    // Convert User entity to UserResponse DTO
    private UserResponse convertToResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getPhone()
        );
    }
}