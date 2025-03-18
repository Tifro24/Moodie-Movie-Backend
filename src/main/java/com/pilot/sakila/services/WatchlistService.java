package com.pilot.sakila.services;

import com.pilot.sakila.entities.Film;
import com.pilot.sakila.entities.Preferences;
import com.pilot.sakila.entities.Watchlist;
import com.pilot.sakila.repository.FilmRepository;
import com.pilot.sakila.repository.PreferencesRepository;
import com.pilot.sakila.repository.WatchlistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@SessionScope
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final PreferencesRepository preferencesRepository;
    private final FilmRepository filmRepository;

    public WatchlistService(WatchlistRepository watchlistRepository,
                            PreferencesRepository preferencesRepository,
                            FilmRepository filmRepository){
        this.watchlistRepository = watchlistRepository;
        this.preferencesRepository = preferencesRepository;
        this.filmRepository = filmRepository;
    }

    public List<Watchlist> getAllWatchlists(){
        return watchlistRepository.findAll();
    }

    public List<Watchlist> getWatchlistsByMood(Short moodId){
        return watchlistRepository.findByMoodId(moodId);
    }

    public List<Watchlist> getUserWatchlists(String sessionId){
        return watchlistRepository.findBySessionId(sessionId);
    }

    public Watchlist createWatchlist(String sessionId, String name, Short moodId){
        if (sessionId == null || sessionId.isBlank()) {
            sessionId = UUID.randomUUID().toString();
        }

        boolean watchlistExists = watchlistRepository.existsBySessionIdAndName(sessionId, name);

        if (watchlistExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A watchlist with this name already exists.");
        }
        Preferences mood = preferencesRepository.findById(moodId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mood not found"));

        Watchlist watchlist = new Watchlist();
        watchlist.setSessionId(sessionId);
        watchlist.setName(name);
        watchlist.setMood(mood);
        watchlist.setFilms(new ArrayList<>());
        return watchlistRepository.save(watchlist);
    }

    public List<Film> searchFilms(String title){
        return filmRepository.findByTitleContainingIgnoreCase(title);
    }

    public Watchlist addFilmToWatchlist(Short watchlistId, Short filmId){
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Watchlist not found"));

        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found"));

        if (watchlist.getFilms().contains(film)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This film is already in the watchlist");
        }
        watchlist.getFilms().add(film);
        return watchlistRepository.save(watchlist);
    }

    public Watchlist removeFilmFromWatchlist(Short watchlistId, Short filmId){
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Watchlist not found"));

        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found"));

        watchlist.getFilms().remove(film);
        return watchlistRepository.save(watchlist);
    }

    public void deleteWatchlist(Short watchlistId){
        if (!watchlistRepository.existsById(watchlistId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Watchlist not found.");
        }
        watchlistRepository.deleteById(watchlistId);
    }

}
