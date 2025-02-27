package com.pilot.sakila.controller;


import com.pilot.sakila.dto.ValidationGroup;
import com.pilot.sakila.dto.request.FilmRequest;
import com.pilot.sakila.dto.response.FilmResponse;
import com.pilot.sakila.dto.response.PartialActorResponse;
import com.pilot.sakila.entities.Actor;
import com.pilot.sakila.entities.Film;
import com.pilot.sakila.repository.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.pilot.sakila.dto.ValidationGroup.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final ActorRepository actorRepository;
    private final FilmRepository filmRepository;
    private final LanguageRepository languageRepository;
    private final FilmActorRepository filmActorRepository;
    private final CategoryRepository categoryRepository;
    private final FilmTextRepository filmTextRepository;

    @Autowired
    public FilmController(FilmRepository filmRepository, ActorRepository actorRepository,
                          LanguageRepository languageRepository,
                          FilmActorRepository filmActorRepository,
                          CategoryRepository categoryRepository,
                          FilmTextRepository filmTextRepository) {
        this.filmRepository = filmRepository;
        this.actorRepository = actorRepository;
        this.languageRepository = languageRepository;
        this.filmActorRepository = filmActorRepository;
        this.categoryRepository = categoryRepository;
        this.filmTextRepository = filmTextRepository;
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

    @GetMapping("/{id}")
    public FilmResponse getFilmById(@PathVariable Short id){
        return filmRepository.findById(id)
                .map(FilmResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "A film with this ID does not exist"));
    }

    @PostMapping
    public FilmResponse createFilm(@RequestBody @Validated(Create.class) FilmRequest data){
        final var film = new Film();
        film.setTitle(data.getTitle());
        film.setDescription(data.getDescription());
        film.setReleaseYear(Year.of(data.getRelease_year()));

        final var language = languageRepository.findById(data.getLanguage_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is not a language with this Id"));

        film.setLanguage(language);

        film.setLength(data.getLength());
        film.setRating(data.getRating());

        final var categories = categoryRepository.findAllById(data.getCategoryIds());
        film.setCategories(new ArrayList<>(categories));

        final var cast = data.getActorIds().stream()
                .map(actorId -> actorRepository.findById(actorId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No actor with ID: " + actorId + " has been found"))
                            ).toList();

        film.setCast(new ArrayList<>(cast));
        final var savedFilm = filmRepository.save(film);
        return FilmResponse.from(savedFilm);
    }

    @PatchMapping("/{id}")
    public FilmResponse updateFilm(@PathVariable Short id, @RequestBody @Validated(Update.class) FilmRequest data){

        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No film with ID: " + id + " has been found"));

        if(data.getTitle() != null){
            film.setTitle(data.getTitle());
        }

        if(data.getDescription() != null){
            film.setDescription(data.getDescription());
        }

        if(data.getLanguage_id() != null){
            final var language = languageRepository.findById(data.getLanguage_id())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No language with that ID has been found"));

            film.setLanguage(language);

        }

        if(data.getRelease_year() != null){
            film.setReleaseYear(Year.of(data.getRelease_year()));
        }

        if(data.getLength() != null){
            film.setLength(data.getLength());
        }

        if(data.getRating() != null){
            film.setRating(data.getRating());
        }

        if(data.getCategoryIds() != null && !data.getCategoryIds().isEmpty()){
            final var categories = data.getCategoryIds().stream()
                    .map(categoryId -> categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No category with id: " + categoryId + " has been found"))
                    ).toList();
            film.setCategories(new ArrayList<>(categories));
        }


        if(data.getActorIds() != null && !data.getActorIds().isEmpty()){

            final var castToAdd = data.getActorIds().stream()
                    .map(actorId -> actorRepository.findById(actorId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No actor with id: " + actorId + " has been found."))
                    ).collect(Collectors.toList());

            List<Actor> currentCast = film.getCast().stream()
                    .map(partialActorResponse -> actorRepository.findById(partialActorResponse.getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "JNFKJNF SJF K"))
                    ).collect(Collectors.toList());

            castToAdd.stream()
                    .filter(actor -> currentCast.stream().noneMatch(a -> a.getId() == actor.getId()))
                    .forEach(currentCast::add);

            film.setCast(currentCast);
        }

        Film updatedFilm = filmRepository.save(film);
        return FilmResponse.from(updatedFilm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFilm(@PathVariable Short id){
        if(!filmRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No film with id: " + id + " has been found");
        }

        filmActorRepository.deleteByFilmId(id);

        filmTextRepository.deleteByFilmId(id);

        filmRepository.deleteById(id);

        return ResponseEntity.ok("Film with id: " + id + " has been successfully deleted");

    }


}
