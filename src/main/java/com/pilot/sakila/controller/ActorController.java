package com.pilot.sakila.controller;



import com.pilot.sakila.dto.request.ActorRequest;
import com.pilot.sakila.dto.response.ActorResponse;
import com.pilot.sakila.services.ActorService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

import static com.pilot.sakila.dto.ValidationGroup.*;

@RestController
@RequestMapping("/actors")

public class ActorController {

    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService){
        this.actorService = actorService;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @GetMapping
    public List<ActorResponse> listActors(@RequestParam(required = false) Optional<String> name){

        final var actors = actorService.listActors(name);
        return actors.stream().map(ActorResponse::from).toList();

    }

    @GetMapping("/{id}")
    public ActorResponse getActorById(@PathVariable Short id){
        final var actor = actorService.getActorById(id);
        return ActorResponse.from(actor);
    }

    @PostMapping
    public ActorResponse createActor(@RequestParam @NotBlank String firstName,
                                     @RequestParam @NotBlank String lastName,
                                     @RequestParam @NotEmpty List<Short> filmIds){
        final var actor = actorService.createActor(firstName, lastName, filmIds);
        return ActorResponse.from(actor);
    }

    @PatchMapping("/{id}")
    public ActorResponse updateActor(@PathVariable Short id,
                                     @RequestParam(required = false) @Size(min=1, max=45) String firstName,
                                     @RequestParam(required = false) @Size(min=1, max=45) String lastName,
                                     @RequestParam(required = false) @NotEmpty List<Short> filmIds) {
        final var actor = actorService.updateActor(id, firstName, lastName, filmIds);
        return ActorResponse.from(actor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteActor(@PathVariable Short id){
        actorService.deleteActor(id);
        return ResponseEntity.ok("Actor with id: " + id + " has been successfully deleted");
    }
}
