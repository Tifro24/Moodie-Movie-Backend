package com.pilot.sakila.services;

import com.pilot.sakila.dto.ValidationGroup;
import com.pilot.sakila.dto.request.FilmRequest;
import com.pilot.sakila.dto.response.FilmResponse;
import com.pilot.sakila.entities.Actor;
import com.pilot.sakila.entities.Film;
import com.pilot.sakila.enums.Rating;
import com.pilot.sakila.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FilmService {
    private final ActorRepository actorRepository;
    private final FilmRepository filmRepository;
    private final LanguageRepository languageRepository;
    private final FilmActorRepository filmActorRepository;
    private final CategoryRepository categoryRepository;
    private final FilmTextRepository filmTextRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository, ActorRepository actorRepository,
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

    public List<Film> getAllFilms(Optional<String> title){
        return title
                .map(filmRepository::findByTitleContainingIgnoreCase)
                .orElseGet(filmRepository::findAll);

    }

    public Film getFilmById(Short id){
        return filmRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "A film with this ID does not exist"));
    }

    public Film createFilm(String title, String description, Short releaseYear, Short languageId, Short length, Rating rating, List<Short> categoryIds, List<Short> actorIds){
        final var film = new Film();
        film.setTitle(title);
        film.setDescription(description);
        film.setReleaseYear(Year.of(releaseYear));

        final var language = languageRepository.findById(languageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is not a language with this Id"));

        film.setLanguage(language);

        film.setLength(length);
        film.setRating(rating);

        final var categories = categoryRepository.findAllById(categoryIds);
        film.setCategories(new ArrayList<>(categories));

        final var cast = actorIds.stream()
                .map(actorId -> actorRepository.findById(actorId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No actor with ID: " + actorId + " has been found"))
                ).toList();

        film.setCast(new ArrayList<>(cast));
        return filmRepository.save(film);
    }

    public Film updateFilm(Short id, String title, String description, Short releaseYear, Short languageId, Short length, Rating rating, List<Short> categoryIds, List<Short> actorIds){

        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No film with ID: " + id + " has been found"));

        if(title != null){
            film.setTitle(title);
        }

        if(description != null){
            film.setDescription(description);
        }

        if(languageId != null){
            final var language = languageRepository.findById(languageId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No language with that ID has been found"));

            film.setLanguage(language);

        }

        if(releaseYear != null){
            film.setReleaseYear(Year.of(releaseYear));
        }

        if(length != null){
            film.setLength(length);
        }

        if(rating != null){
            film.setRating(rating);
        }

        if(categoryIds!= null && !categoryIds.isEmpty()){
            final var categories = categoryIds.stream()
                    .map(categoryId -> categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No category with id: " + categoryId + " has been found"))
                    ).toList();
            film.setCategories(new ArrayList<>(categories));
        }


        if(actorIds != null && !actorIds.isEmpty()){

            final var castToAdd = actorIds.stream()
                    .map(actorId -> actorRepository.findById(actorId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No actor with id: " + actorId + " has been found."))
                    ).toList();

            List<Actor> currentCast = film.getCast().stream()
                    .map(partialActorResponse -> actorRepository.findById(partialActorResponse.getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "JNFKJNF SJF K"))
                    ).collect(Collectors.toList());

            castToAdd.stream()
                    .filter(actor -> currentCast.stream().noneMatch(a -> a.getId() == actor.getId()))
                    .forEach(currentCast::add);

            film.setCast(currentCast);
        }

        return filmRepository.save(film);

    }

    public void deleteFilm(Short id){
        if(!filmRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No film with id: " + id + " has been found");
        }
        filmActorRepository.deleteByFilmId(id);
        filmTextRepository.deleteByFilmId(id);
        filmRepository.deleteById(id);
    }


}
