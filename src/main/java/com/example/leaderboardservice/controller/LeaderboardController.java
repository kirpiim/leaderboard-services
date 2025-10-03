package com.example.leaderboardservice.controller;

import com.example.leaderboardservice.models.LeaderboardEntry;
import com.example.leaderboardservice.models.Score;
import com.example.leaderboardservice.models.User;
import com.example.leaderboardservice.repositories.ScoreRepository;
import com.example.leaderboardservice.repositories.UserRepository;
import com.example.leaderboardservice.service.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public LeaderboardController(ScoreRepository scoreRepository, JwtUtil jwtUtil) {
        this.scoreRepository = scoreRepository;
        this.jwtUtil = jwtUtil;
    }

    // POST /leaderboard/scores → save score for logged-in user
    @PostMapping("/scores")
    public ResponseEntity<?> submitScore(@RequestParam long points) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // username is email

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Score score = new Score();
        score.setUser(user);
        score.setPoints(points);

        scoreRepository.save(score);

        return ResponseEntity.ok("Score saved successfully!");
    }

    // GET /leaderboard?limit=N → return top N scores (only username, points, timestamp)
    @GetMapping
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard(@RequestParam(defaultValue = "10") int limit) {
        List<Score> scores = scoreRepository.findAllByOrderByPointsDescTimestampAsc(PageRequest.of(0, limit));

        List<LeaderboardEntry> leaderboard = scores.stream()
                .map(score -> new LeaderboardEntry(
                        score.getUser().getUsername(),
                        score.getPoints(),
                        score.getTimestamp()
                ))
                .toList();

        return ResponseEntity.ok(leaderboard);
    }
}
