package com.pilot.sakila.controller;



import com.pilot.sakila.dto.request.FilmRequest;
import com.pilot.sakila.dto.response.FilmResponse;
import com.pilot.sakila.entities.Film;
import com.pilot.sakila.enums.Rating;
import com.pilot.sakila.services.FilmService;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import static com.pilot.sakila.dto.ValidationGroup.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;


    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;

    }

    @GetMapping
    public List<FilmResponse> getAllFilms(@RequestParam(required = false) Optional<String> title){
        final var films = filmService.getAllFilms(title);
        return films.stream().map(FilmResponse::from).toList();
    }

    @GetMapping("/{id}")
    public FilmResponse getFilmById(@PathVariable Short id){
        final var film = filmService.getFilmById(id);
        return FilmResponse.from(film);
    }

    @GetMapping("/byMood")
    public List<FilmResponse> getMoviesByMood(@RequestParam String mood) {

        List<String> genres = mapGenresToMood(mood);

        List<Film> films = filmService.getFilmsByGenres(genres);

        return films.stream()
                .map(FilmResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/byGenre")
    public List<FilmResponse> getMoviesByGenre(@RequestParam String genre) {
        List<Film> films = filmService.getFilmsByCategoryName(genre);
        return films.stream()
                .map(FilmResponse::from)
                .collect(Collectors.toList());
    }


    private List<String> mapGenresToMood(String mood) {
        switch (mood.toLowerCase()) {
            case "happy": return Arrays.asList("Comedy", "Family");
            case "sad" : return Arrays.asList("Drama", "Romance");
            case "adventurous" : return Arrays.asList("Action", "Adventure");
            case "romantic" : return Arrays.asList("Romance", "Drama");
            case "funny" : return Arrays.asList("Comedy");
            case "excited" : return Arrays.asList("Action", "Adventure");
            case "scary" : return Arrays.asList("Horror", "Thriller");
            default: return Collections.emptyList();
        }
    }

    @PostMapping
    public FilmResponse createFilm(@RequestParam @NotBlank String title,
                                   @RequestParam @NotBlank String description,
                                   @RequestParam @Min(1901) @Max(2150) Short releaseYear,
                                   @RequestParam @NotNull Short languageId,
                                   @RequestParam @Min(1) Short length,
                                   @RequestParam @NotNull Rating rating,
                                   @RequestParam @NotEmpty List<Short> categoryIds,
                                   @RequestParam @NotEmpty List<Short> actorIds){
       final var film = filmService.createFilm(title, description, releaseYear, languageId, length, rating, categoryIds, actorIds);
       return FilmResponse.from(film);
    }

    @PatchMapping("/{id}")
    public FilmResponse updateFilm(@PathVariable Short id,
                                   @RequestParam(required = false) @Size(min=1, max=45) String title,
                                   @RequestParam(required = false) @Size(min=1) String description,
                                   @RequestParam(required = false) @Min(1901) @Max(2150) Short releaseYear,
                                   @RequestParam(required = false) @NotNull Short languageId,
                                   @RequestParam(required = false) @Min(1) Short length,
                                   @RequestParam(required = false) @NotNull Rating rating,
                                   @RequestParam(required = false) @NotEmpty List<Short> categoryIds,
                                   @RequestParam(required = false) @NotEmpty List<Short> actorIds){
        final var film = filmService.updateFilm(id, title, description, releaseYear, languageId, length, rating, categoryIds, actorIds);
        return FilmResponse.from(film);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFilm(@PathVariable Short id){
        filmService.deleteFilm(id);
        return ResponseEntity.ok("Film with id: " + id + " has been successfully deleted");

    }


}
