package com.example.leaderboardservice.dto;

import java.time.Instant;

public class LeaderboardEntry {
    private String username;
    private long points;
    private Instant timestamp;

    public LeaderboardEntry(String username, long points, Instant timestamp) {
        this.username = username;
        this.points = points;
        this.timestamp = timestamp;
    }

    public String getUsername() { return username; }
    public long getPoints() { return points; }
    public Instant getTimestamp() { return timestamp; }
}

