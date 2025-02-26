package com.pilot.sakila.dto.response;

import com.pilot.sakila.entities.Film;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class FilmResponse {
    private final Short id;
    private final String title;
    private final String description;
    private final String language;
    private final List<PartialActorResponse> cast;

    public static FilmResponse from(Film film){
        return new FilmResponse(
                film.getId(),
                film.getTitle(),
                film.getDescription(),
                film.getLanguage().getName(),
                film.getCast()
        );
    }
}
