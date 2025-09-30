package com.example.leaderboardservice.service;

import com.example.leaderboardservice.dto.AuthResponse;
import com.example.leaderboardservice.dto.LoginRequest;
import com.example.leaderboardservice.dto.RegisterRequest;
import com.example.leaderboardservice.models.User;
import com.example.leaderboardservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthResponse(false, "Email already registered", null);
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername()); //  save username
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return new AuthResponse(true, "User registered successfully", null);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse(false, "Invalid password", null);
        }

        //  include email (or both email + username) in token
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(true, "Login successful", token);
    }
}

