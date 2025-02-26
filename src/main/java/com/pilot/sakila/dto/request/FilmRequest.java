package com.pilot.sakila.dto.request;

import com.pilot.sakila.entities.Language;
import com.pilot.sakila.enums.Rating;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Year;
import java.util.List;

@AllArgsConstructor
@Getter

public class FilmRequest {

    private final String title;
    private final String description;
    private final Short release_year;
    private final Short language_id;
    private final Short length;
    private final Rating rating;
    private final List<Short> categoryIds;
    private final List<Short> actorIds;

}
