package com.example.smartlock.service;

import com.example.smartlock.model.dto.auth.AuthResponse;
import com.example.smartlock.model.dto.auth.LoginRequest;
import com.example.smartlock.model.dto.auth.RegisterRequest;
import com.example.smartlock.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    protected AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse registerUser(RegisterRequest request) {

        String passwordHash = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getEmail(),
                passwordHash,
                request.getFullName(),
                OffsetDateTime.now()
        );

        user = userService.saveUser(user).orElseThrow(() -> new RuntimeException()); //error saving user

        String token = jwtService.generateToken(user.getEmail());

            return new AuthResponse(
                    user.getUserId(),
                    token,
                    user.getEmail(),
                    user.getFullName()
            );


    }

    public AuthResponse loginUser(LoginRequest request) {
        User user = userService.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException()); // no user with this email

        if (passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            String token = jwtService.generateToken(user.getEmail());

            return new AuthResponse(
                    user.getUserId(),
                    token,
                    user.getEmail(),
                    user.getFullName()
            );
        } else {
            throw new RuntimeException(); // password dont matches
        }

    }
}


