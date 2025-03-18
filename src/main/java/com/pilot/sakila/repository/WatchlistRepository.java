package com.pilot.sakila.repository;

import com.pilot.sakila.entities.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, Short> {
    List<Watchlist> findByMoodId(Short moodId); // Get watchlists by mood
    List<Watchlist> findBySessionId(String sessionId);
    boolean existsBySessionIdAndName(String sessionId, String name);
}
