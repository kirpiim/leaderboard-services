package com.example.leaderboardservice.repositories;

import com.example.leaderboardservice.models.LeaderboardEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {
    // convenience method for ordering; your controller can call this
    List<LeaderboardEntry> findAllByOrderByPointsDesc();
}
