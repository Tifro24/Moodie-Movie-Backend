package com.pilot.sakila.dto.response;

import com.pilot.sakila.entities.Category;
import com.pilot.sakila.entities.Film;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class FilmResponse {
    private final Short id;
    private final String title;
    private final String description;
    private final String language;
    private final List<CategoryResponse> categories;
    private final List<PartialActorResponse> cast;

    public static FilmResponse from(Film film){

        List<CategoryResponse> categoryResponses = film.getCategories().stream()
                .map(category -> new CategoryResponse(category.getName()))
                .collect(Collectors.toList());



        return new FilmResponse(
                film.getId(),
                film.getTitle(),
                film.getDescription(),
                film.getLanguage().getName(),
                categoryResponses,
                film.getCast()
        );
    }
}
