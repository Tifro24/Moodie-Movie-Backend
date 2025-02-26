package com.pilot.sakila.dto.response;

import com.pilot.sakila.entities.Actor;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class PartialActorResponse {
    private final Short id;
    private final String fullName;

    public static PartialActorResponse from(Actor actor){
        return new PartialActorResponse(actor.getId(), actor.getFullName());
    }
}
