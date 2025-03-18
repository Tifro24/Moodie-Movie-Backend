package com.pilot.sakila.controller;

import com.pilot.sakila.dto.response.FilmResponse;
import com.pilot.sakila.dto.response.MiniFilmResponse;
import com.pilot.sakila.dto.response.WatchlistResponse;
import com.pilot.sakila.entities.Film;
import com.pilot.sakila.entities.Watchlist;
import com.pilot.sakila.services.FilmService;
import com.pilot.sakila.services.WatchlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {
    private final WatchlistService watchlistService;
    private final FilmService filmService;

    public WatchlistController(WatchlistService watchlistService, FilmService filmService){
        this.watchlistService = watchlistService;
        this.filmService = filmService;
    }

    @GetMapping
    public List<WatchlistResponse> getAllWatchlists(){
        List<Watchlist> watchlists = watchlistService.getAllWatchlists();
        return watchlists.stream().map(WatchlistResponse::from).collect(Collectors.toList());
    }

    @GetMapping("/{moodId}")
    public List<WatchlistResponse> getWatchlistsByMood(@PathVariable Short moodId){
        List<Watchlist> watchlists = watchlistService.getWatchlistsByMood(moodId);
        return watchlists.stream().map(WatchlistResponse::from).collect(Collectors.toList());
    }

    @GetMapping("/user")
    public List<WatchlistResponse> getUserWatchlists(@RequestParam String sessionId) {
        List<Watchlist> watchlists = watchlistService.getUserWatchlists(sessionId);
        return watchlists.stream().map(WatchlistResponse::from).collect(Collectors.toList());
    }

    @PostMapping
    public WatchlistResponse createWatchlist(@RequestParam(required = false) String sessionId, @RequestParam String name, @RequestParam Short moodId) {
        Watchlist watchlist = watchlistService.createWatchlist(sessionId, name, moodId);
        return WatchlistResponse.from(watchlist);
    }

    @GetMapping("/search-films")
    public List<MiniFilmResponse> searchFilms(@RequestParam Optional<String> title) {
        List<Film> films = filmService.getAllFilms(title);
        return films.stream().map(MiniFilmResponse::from).collect(Collectors.toList());
    }

    @PostMapping("/{watchlistId}/add-film")
    public WatchlistResponse addFilmToWatchlist(@PathVariable Short watchlistId, @RequestParam Short filmId) {
        Watchlist watchlist = watchlistService.addFilmToWatchlist(watchlistId, filmId);
        return WatchlistResponse.from(watchlist);
    }

    @DeleteMapping("/{watchlistId}/remove-film/{filmId}")
    public WatchlistResponse removeFromWatchlist(@PathVariable Short watchlistId, @PathVariable Short filmId) {
        Watchlist watchlist = watchlistService.removeFilmFromWatchlist(watchlistId, filmId);
        return WatchlistResponse.from(watchlist);
    }

    @DeleteMapping("/{watchlistId}")
    public ResponseEntity<String> deleteWatchlist(@PathVariable Short watchlistId) {
        watchlistService.deleteWatchlist(watchlistId);
        return ResponseEntity.ok("Watchlist successfully deleted");
    }



}
