package com.pilot.sakila.dto.request;

import com.pilot.sakila.dto.ValidationGroup;
import com.pilot.sakila.entities.Language;
import com.pilot.sakila.enums.Rating;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Year;
import java.util.List;

import static com.pilot.sakila.dto.ValidationGroup.*;

@AllArgsConstructor
@Getter

public class FilmRequest {

    @NotNull(groups = {Create.class})
    private final String title;

    @NotNull(groups = {Create.class})
    private final String description;

    @NotNull(groups = {Create.class})
    @Min(value = 1901, groups = Create.class)
    @Max(value = 2150, groups = Create.class)
    private final Short release_year;

    @NotNull(groups = {Create.class})
    private final Short language_id;

    @NotNull(groups = {Create.class})
    @Min(value = 1, groups = Create.class)
    private final Short length;

    @NotNull(groups = {Create.class})
    private final Rating rating;

    @NotNull(groups = {Create.class})
    @Size(min = 1, message = "At least one category ID is required", groups = Create.class)
    private final List<Short> categoryIds;

    @NotNull(groups = {Create.class})
    @Size(min = 1, message = "At least one actor ID is required", groups = Create.class)
    private final List<Short> actorIds;

}
