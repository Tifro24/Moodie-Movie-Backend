package com.pilot.sakila.dto;

import com.pilot.sakila.controller.ActorController;
import com.pilot.sakila.dto.response.ActorResponse;
import com.pilot.sakila.entities.Actor;
import com.pilot.sakila.services.ActorService;
import org.apache.coyote.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ActorControllerTest {

    static ActorService service = mock(ActorService.class);
    static ActorController controller = new ActorController(service);

    static List<Actor> actors = List.of(
            new Actor((short) 1, "Elijah", "Duma", "Elijah Duma", List.of()),
            new Actor((short) 2, "Samuel", "Ojo", "Samuel Ojo", List.of()),
            new Actor((short) 3, "Charles", "Owusu", "Charles Owusu", List.of()),
            new Actor((short) 4, "Goku", "Kakaroto", "Goku Kakaroto", List.of()),
            new Actor((short) 5, "Isagi", "Yoichi", "Isagi Yoichi", List.of())

    );

    @BeforeAll
    public static void setup() {

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(service).getActorById(anyShort());
        for(var actor : actors){
            doReturn(actor).when(service).getActorById(actor.getId());
        }

    }


    @Test
    public void getActorReturnsActorResponseForAValidActorId(){
        final var expectedResponse = ActorResponse.from(actors.get(0));
        final var actualResponse = controller.getActorById((short) 1);
        Assertions.assertEquals(expectedResponse.getId(), actualResponse.getId());
        Assertions.assertEquals(expectedResponse.getFirstName(), actualResponse.getFirstName());
        Assertions.assertEquals(expectedResponse.getLastName(), actualResponse.getLastName());
    }

    @Test
    public void listActorsReturnActorsOfActorResponsesForStringContainedInName() {
        List<Actor> expectedActors = List.of(actors.get(0), actors.get(1));
        doReturn(expectedActors).when(service).listActors(Optional.of("El"));

        List<ActorResponse> actualResponses = controller.listActors(Optional.of("El"));

        assertNotNull(actualResponses);
        assertEquals(expectedActors.size(), actualResponses.size());

        for(int i = 0; i < expectedActors.size(); i++){
            Assertions.assertEquals(ActorResponse.from(expectedActors.get(i)).getId(), actualResponses.get(i).getId());
            Assertions.assertEquals(ActorResponse.from(expectedActors.get(i)).getFirstName(), actualResponses.get(i).getFirstName());
            Assertions.assertEquals(ActorResponse.from(expectedActors.get(i)).getLastName(), actualResponses.get(i).getLastName());

        }


    }

    @Test
    public void listActorsDoesNotReturnActorsOfActorResponsesForStringNotContainedInName() {
        List<Actor> expectedActors = List.of(actors.get(0), actors.get(1));
        doReturn(expectedActors).when(service).listActors(Optional.of("El"));

        List<ActorResponse> actualResponses = controller.listActors(Optional.of("El"));

        assertNotNull(actualResponses);
        assertEquals(expectedActors.size(), actualResponses.size());

        List<String> expectedNames = expectedActors.stream().map(Actor::getFirstName).toList();

        for(ActorResponse response : actualResponses){
            Assertions.assertTrue(expectedNames.contains(response.getFirstName()), "Unexpected actor found in the result: " + response.getFirstName());

        }


    }

    @Test
    public void listActorsReturnActorsOfActorResponsesForEmptyOptional() {
        List<Actor> expectedActors = actors;
        doReturn(expectedActors).when(service).listActors(Optional.empty());

        List<ActorResponse> actualResponses = controller.listActors(Optional.empty());

        assertNotNull(actualResponses);
        assertEquals(expectedActors.size(), actualResponses.size());

        List<String> expectedNames = expectedActors.stream().map(Actor::getFirstName).toList();

        for(int i = 0; i < expectedActors.size(); i++){
            Assertions.assertEquals(ActorResponse.from(expectedActors.get(i)).getId(), actualResponses.get(i).getId());
            Assertions.assertEquals(ActorResponse.from(expectedActors.get(i)).getFirstName(), actualResponses.get(i).getFirstName());
            Assertions.assertEquals(ActorResponse.from(expectedActors.get(i)).getLastName(), actualResponses.get(i).getLastName());

        }


    }

    @Test
    public void createActorsReturnActorResponseOfCreatedActor(){
        Actor actorToBeCreated = new Actor((short) 6, "Naruto", "Uzumaki", "Naruto Uzumaki", List.of());

        doReturn(actorToBeCreated).when(service).createActor(anyString(), anyString(), anyList());

        ActorResponse actualActor = controller.createActor("Naruto", "Uzumaki", List.of());

        assertNotNull(actualActor);
        Assertions.assertEquals(actorToBeCreated.getId(), actualActor.getId());
        Assertions.assertEquals(actorToBeCreated.getFirstName(), actualActor.getFirstName());
        Assertions.assertEquals(actorToBeCreated.getLastName(), actualActor.getLastName());
    }

    @Test
    public void updateActorUpdatedReturnActorResponseOfUpdatedActor(){
         Actor actorToUpdate = actors.get(0);

         actorToUpdate.setFirstName("Terrence");

         doReturn(actorToUpdate).when(service).updateActor(actorToUpdate.getId(), "Terrence", (null), (null));

         ActorResponse actualUpdatedActor = controller.updateActor((short) 1, "Terrence",(null), (null));

         assertNotNull(actualUpdatedActor);
         Assertions.assertEquals(actorToUpdate.getId(), actualUpdatedActor.getId());
         Assertions.assertEquals("Terrence", actualUpdatedActor.getFirstName());
         Assertions.assertEquals(actorToUpdate.getLastName(), actualUpdatedActor.getLastName());
    }
}
