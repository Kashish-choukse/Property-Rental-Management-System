package com.kashish.rental.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kashish.rental.dto.LoginResponse;
import com.kashish.rental.dto.UserResponse;
import com.kashish.rental.entity.User;
import com.kashish.rental.repository.PropertyRepository;
import com.kashish.rental.repository.RentalRepository;
import com.kashish.rental.repository.UserRepository;
import com.kashish.rental.security.JwtService;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PropertyRepository propertyRepository;
    private final RentalRepository rentalRepository;

    public UserService(
            UserRepository repository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            PropertyRepository propertyRepository,
            RentalRepository rentalRepository) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.propertyRepository = propertyRepository;
        this.rentalRepository = rentalRepository;
    }

    // =========================
    // ADD USER / REGISTER
    // =========================
    public User addUser(User user) {

        // Check duplicate email
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        // Public registration is only for TENANT
        user.setRole("TENANT");

        // Encrypt password before saving
        String encodedPassword =
                passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        return repository.save(user);
    }

    // =========================
    // GET ALL USERS
    // =========================
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    // =========================
    // GET USER BY ID
    // =========================
    public User getUserById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found"
                        )
                );
    }

    // =========================
    // LOGIN
    // =========================
    public LoginResponse login(
            String email,
            String password) {

        User user = repository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Invalid email or password"
                        )
                );

        // Check password
        if (!passwordEncoder.matches(
                password,
                user.getPassword())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        // Generate JWT token
        String token = jwtService.generateToken(user);

        // Create UserResponse
        UserResponse userResponse =
                new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getPhone()
                );

        return new LoginResponse(
                token,
                userResponse
        );
    }

    // =========================
    // DELETE USER
    // =========================
    public void deleteUser(Long id, String email) {

        User user = getUserById(id);

        // User can delete only their own account
        if (!user.getEmail().equalsIgnoreCase(email)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can delete only your own account"
            );
        }

        // Check whether user owns any properties
        if (propertyRepository.existsByOwnerId(id) == 1) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete account because the user owns properties"
            );
        }

        // Check whether user has any rentals
        if (rentalRepository.existsByTenantId(id) == 1) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete account because the user has rental records"
            );
        }

        // Delete user
        repository.delete(user);
    }
}