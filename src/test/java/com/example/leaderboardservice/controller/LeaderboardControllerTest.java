package com.example.leaderboardservice.controller;

import com.example.leaderboardservice.LeaderboardServiceApplication;
import com.example.leaderboardservice.models.Score;
import com.example.leaderboardservice.models.User;
import com.example.leaderboardservice.repositories.ScoreRepository;
import com.example.leaderboardservice.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LeaderboardServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false) // disables Spring Security filters
@ActiveProfiles("test")
class LeaderboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDb() {
        scoreRepository.deleteAll();
        userRepository.deleteAll();

        // Insert a test user into DB
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("player1");
        user.setPassword("password");
        userRepository.save(user);

        // Mock authentication for that user
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user.getEmail(), null, Collections.emptyList())
        );
    }

    @Test
    void submitScore_Success() throws Exception {
        mockMvc.perform(post("/leaderboard/scores")
                        .param("points", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Score saved successfully!"));
    }

    @Test
    void getLeaderboard_Success() throws Exception {
        // Save a score manually
        User user = userRepository.findByEmail("test@example.com").orElseThrow();
        Score score = new Score();
        score.setUser(user);
        score.setPoints(200);
        scoreRepository.save(score);

        mockMvc.perform(get("/leaderboard")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("player1"))
                .andExpect(jsonPath("$[0].points").value(200));
    }

    @Test
    void getLeaderboard_Empty() throws Exception {
        mockMvc.perform(get("/leaderboard")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]")); // expects empty JSON array
    }

    @Test
    void getLeaderboard_MultipleUsersSorted() throws Exception {
        // Create 3 users
        User user1 = new User();
        user1.setEmail("a@example.com");
        user1.setUsername("Alice");
        user1.setPassword("pass");
        userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("b@example.com");
        user2.setUsername("Bob");
        user2.setPassword("pass");
        userRepository.save(user2);

        User user3 = new User();
        user3.setEmail("c@example.com");
        user3.setUsername("Charlie");
        user3.setPassword("pass");
        userRepository.save(user3);

        // Give them scores
        Score score1 = new Score();
        score1.setUser(user1);
        score1.setPoints(50);
        scoreRepository.save(score1);

        Score score2 = new Score();
        score2.setUser(user2);
        score2.setPoints(100);
        scoreRepository.save(score2);

        Score score3 = new Score();
        score3.setUser(user3);
        score3.setPoints(75);
        scoreRepository.save(score3);

        // Now request leaderboard
        mockMvc.perform(get("/leaderboard").param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("Bob"))     // 100 points
                .andExpect(jsonPath("$[1].username").value("Charlie")) // 75 points
                .andExpect(jsonPath("$[2].username").value("Alice"));  // 50 points
    }

}
