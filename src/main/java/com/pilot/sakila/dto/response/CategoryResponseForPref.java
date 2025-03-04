package com.pilot.sakila.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@AllArgsConstructor
@Getter
public class CategoryResponseForPref {


    @AllArgsConstructor
    @Getter
    public static class FilmResponse {
        private final String title;
        private final String description;
    }
    @AllArgsConstructor
    @Getter
    public static class CategoryResponse {
        private final Short preferenceId;  // Preference ID
        private final String mood;        // Mood (e.g. happy, sad)
        private final String category;    // Category name (e.g. Action, Drama)
        private final List<FilmResponse> films;  // List of films with titles and descriptions
    }

}
