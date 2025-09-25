package com.example.leaderboardservice;

import com.example.leaderboardservice.models.Score;
import com.example.leaderboardservice.models.User;
import com.example.leaderboardservice.repositories.ScoreRepository;
import com.example.leaderboardservice.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LeaderboardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeaderboardServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(UserRepository userRepository, ScoreRepository scoreRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            // 1️⃣ Delete old demo user and related scores
            userRepository.findByEmail("test@example.com").ifPresent(existing -> {
                // Delete related scores first
                scoreRepository.deleteAllByUserId(existing.getId());
                // Delete the user
                userRepository.delete(existing);
                System.out.println("Deleted old demo user and related scores");
            });

            // 2️⃣ Insert new demo user with bcrypt password
            userRepository.findByEmail("test@example.com").ifPresentOrElse(
                    existing -> System.out.println("User already exists: " + existing.getEmail()),
                    () -> {
                        var user = new User();
                        user.setUsername("testuser");
                        user.setEmail("test@example.com");
                        // Encode the password using bcrypt
                        user.setPassword(passwordEncoder.encode("password"));
                        userRepository.save(user);
                        System.out.println("Inserted demo user with encoded password");
                    }
            );
        };
    }
}
