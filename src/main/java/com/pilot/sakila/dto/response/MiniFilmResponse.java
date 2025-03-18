package com.pilot.sakila.dto.response;

import com.pilot.sakila.entities.Film;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MiniFilmResponse {

    private final Short id;
    private final String title;

    public static MiniFilmResponse from(Film film) {
        return new MiniFilmResponse(film.getId(), film.getTitle());
    }
}
