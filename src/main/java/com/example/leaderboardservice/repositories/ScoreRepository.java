package com.example.leaderboardservice.repositories;

import com.example.leaderboardservice.models.Score;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findAllByOrderByPointsDescTimestampAsc(Pageable pageable);

    // Delete all scores by user ID
    void deleteAllByUserId(Long userId);
}

