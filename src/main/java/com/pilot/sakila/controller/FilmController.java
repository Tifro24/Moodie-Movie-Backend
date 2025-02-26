package com.pilot.sakila.controller;


import com.pilot.sakila.dto.response.FilmResponse;
import com.pilot.sakila.entities.Film;
import com.pilot.sakila.repository.FilmRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FilmController {

    @Autowired
    private FilmRepository filmRepository;

    @GetMapping("/films")
    public List<FilmResponse> getAllFilms(){
        List<Film> films = filmRepository.findAll();

        return films.stream()
                .map(FilmResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/films/{id}")
    public FilmResponse getFilmById(@PathVariable Short id){
        return filmRepository.findById(id)
                .map(FilmResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "A film with this ID does not exist"));
    }
}
