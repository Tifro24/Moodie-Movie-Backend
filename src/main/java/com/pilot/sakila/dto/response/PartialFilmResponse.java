package com.pilot.sakila.dto.response;

import com.pilot.sakila.entities.Film;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Year;

@AllArgsConstructor
@Getter

public class PartialFilmResponse {
    private final Short id;
    private final String title;
    private final Year releaseYear;

    public static PartialFilmResponse from(Film film){
        return new PartialFilmResponse(
                film.getId(),
                film.getTitle(),
                film.getReleaseYear()
        );

    }
}
