package com.pilot.sakila.controller;


import com.pilot.sakila.dto.request.FilmRequest;
import com.pilot.sakila.dto.response.FilmResponse;
import com.pilot.sakila.entities.Film;
import com.pilot.sakila.repository.ActorRepository;
import com.pilot.sakila.repository.FilmRepository;
import com.pilot.sakila.repository.LanguageRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final ActorRepository actorRepository;
    private final FilmRepository filmRepository;
    private final LanguageRepository languageRepository;

    @Autowired
    public FilmController(FilmRepository filmRepository, ActorRepository actorRepository, LanguageRepository languageRepository){
        this.filmRepository = filmRepository;
        this.actorRepository = actorRepository;
        this.languageRepository = languageRepository;
    }

    @GetMapping
    public List<FilmResponse> getAllFilms(@RequestParam(required = false) Optional<String> title){

        return title
                .map(filmRepository::findByTitleContainingIgnoreCase)
                .orElseGet(filmRepository::findAll)
                .stream()
                .map(FilmResponse::from)
                .toList();
    }

    @GetMapping("/films/{id}")
    public FilmResponse getFilmById(@PathVariable Short id){
        return filmRepository.findById(id)
                .map(FilmResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "A film with this ID does not exist"));
    }

    @PostMapping
    public FilmResponse createFilm(@RequestBody FilmRequest data){
        final var film = new Film();
        film.setTitle(data.getTitle());
        film.setDescription(data.getDescription());
        film.setReleaseYear(Year.of(data.getRelease_year()));

        final var language = languageRepository.findById(data.getLanguage_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is not a language with this Id"));

        film.setLanguage(language);

        film.setLength(data.getLength());

        final var cast = data.getActorIds().stream()
                .map(actorId -> actorRepository.findById(actorId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No actor with ID: " + actorId + " has been found"))
                            ).toList();

        film.setCast(cast);
        final var savedFilm = filmRepository.save(film);
        return FilmResponse.from(savedFilm);
    }
}
