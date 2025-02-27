package com.pilot.sakila.dto.response;

import com.pilot.sakila.entities.Actor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ActorResponse {
    private final Short id;
    private final String firstName;
    private final String lastName;
    private final String fullName;
    private final List<PartialFilmResponse> films;

    public static ActorResponse from(Actor actor){
        return new ActorResponse(
                actor.getId(),
                actor.getFirstName(),
                actor.getLastName(),
                actor.getFullName(),
                actor.getFilms()
                        .stream()
                        .map(PartialFilmResponse::from)
                        .toList()
        );
    }
}

// filtering
// decoupling