package com.pilot.sakila.dto.response;

import com.pilot.sakila.entities.Preferences;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MoodResponse {

    private final Short id;
    private final String mood;

    public static MoodResponse from(Preferences mood) {
        return new MoodResponse(mood.getId(), mood.getMood());
    }
}
