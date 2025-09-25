package com.example.leaderboardservice;

import com.example.leaderboardservice.models.Score;
import com.example.leaderboardservice.models.User;
import com.example.leaderboardservice.repositories.ScoreRepository;
import com.example.leaderboardservice.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LeaderboardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeaderboardServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(UserRepository userRepository, ScoreRepository scoreRepository) {
        return args -> {
            userRepository.findByEmail("test@example.com").ifPresentOrElse(
                    existing -> System.out.println("User already exists: " + existing.getEmail()),
                    () -> {
                        var user = new User();
                        user.setUsername("testuser");
                        user.setEmail("test@example.com");
                        user.setPassword("password");
                        userRepository.save(user);
                        System.out.println("Inserted demo user");
                    }
            );
        };
    }

}

