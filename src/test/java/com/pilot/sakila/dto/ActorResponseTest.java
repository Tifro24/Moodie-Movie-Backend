package com.pilot.sakila.dto;

import com.pilot.sakila.dto.response.ActorResponse;
import com.pilot.sakila.entities.Actor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ActorResponseTest {
    @Test
    public void fromCreatesActorResponseFromActor(){
        final var actor = new Actor((short)1, "Elijah", "Duma", "Elijah Duma", List.of());
        final var result = ActorResponse.from(actor);
        Assertions.assertEquals((short)1, result.getId());
        Assertions.assertEquals("Elijah", result.getFirstName());
        Assertions.assertEquals("Duma", result.getLastName());
    }
}
