package com.pilot.sakila.controller;



import com.pilot.sakila.dto.request.FilmRequest;
import com.pilot.sakila.dto.response.FilmResponse;
import com.pilot.sakila.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Optional;


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
        return filmService.getAllFilms(title);
    }

    @GetMapping("/{id}")
    public FilmResponse getFilmById(@PathVariable Short id){
        return filmService.getFilmById(id);
    }

    @PostMapping
    public FilmResponse createFilm(@RequestBody @Validated(Create.class) FilmRequest data){
       return filmService.createFilm(data);
    }

    @PatchMapping("/{id}")
    public FilmResponse updateFilm(@PathVariable Short id, @RequestBody @Validated(Update.class) FilmRequest data){
        return filmService.updateFilm(id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFilm(@PathVariable Short id){
        filmService.deleteFilm(id);
        return ResponseEntity.ok("Film with id: " + id + " has been successfully deleted");

    }


}
