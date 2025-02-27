package com.pilot.sakila.dto;

import com.pilot.sakila.controller.ActorController;
import com.pilot.sakila.dto.response.ActorResponse;
import com.pilot.sakila.entities.Actor;
import com.pilot.sakila.services.ActorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ActorControllerTest {

    ActorService service = mock(ActorService.class);
    ActorController controller = new ActorController(service);

    @Test
    public void getActorReturnsActorResponseForAValidActorId(){
        final short id = 1;
        final var actor = new Actor(id, "Elijah", "Duma", "Elijah Duma", List.of());

        doReturn(actor).when(service).getActorById(id);


        final var expectedResponse = ActorResponse.from(actor);
        final var actualResponse = controller.getActorById(id);

        Assertions.assertEquals(expectedResponse.getId(), actualResponse.getId());
        Assertions.assertEquals(expectedResponse.getFirstName(), actualResponse.getFirstName());
        Assertions.assertEquals(expectedResponse.getLastName(), actualResponse.getLastName());
    }
}
