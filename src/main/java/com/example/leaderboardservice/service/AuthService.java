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
    private final JwtUtil jwtUtil; //  NEW

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil; //  Inject JwtUtil
    }

    // Register method using RegisterRequest
    public AuthResponse register(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return new AuthResponse(false, "Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        // Encode the password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return new AuthResponse(true, "User registered successfully");
    }

    // Login method using LoginRequest
    public AuthResponse login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return new AuthResponse(false, "User not found");
        }

        User user = optionalUser.get();
        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!matches) {
            return new AuthResponse(false, "Invalid password");
        }

        //  Generate JWT
        String token = jwtUtil.generateToken(user.getUsername());


        //  Return token instead of just message
        return new AuthResponse(true, token);
    }
}
