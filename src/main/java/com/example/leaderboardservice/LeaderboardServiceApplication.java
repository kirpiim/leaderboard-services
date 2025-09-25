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
    CommandLineRunner demo(UserRepository userRepo, ScoreRepository scoreRepo) {
        return args -> {
            User u = new User();
            u.setUsername("testuser");
            u.setEmail("test@example.com");
            u.setPassword("password"); // plain for now; will hash later
            userRepo.save(u);

            Score s = new Score();
            s.setUser(u);
            s.setPoints(123);
            scoreRepo.save(s);

            System.out.println("Saved user id = " + u.getId());
            System.out.println("Score count = " + scoreRepo.count());
        };
    }
}

