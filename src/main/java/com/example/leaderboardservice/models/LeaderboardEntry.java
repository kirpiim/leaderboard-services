package com.example.leaderboardservice.models;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "leaderboard")
public class LeaderboardEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    // use "points" to match your DB and DTO naming
    private long points;

    private Instant timestamp;

    public LeaderboardEntry() {}

    public LeaderboardEntry(String username, long points, Instant timestamp) {
        this.username = username;
        this.points = points;
        this.timestamp = timestamp;
    }

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public long getPoints() { return points; }
    public void setPoints(long points) { this.points = points; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
