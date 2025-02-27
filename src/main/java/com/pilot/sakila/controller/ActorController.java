package com.pilot.sakila.controller;



import com.pilot.sakila.dto.request.ActorRequest;
import com.pilot.sakila.dto.response.ActorResponse;
import com.pilot.sakila.services.ActorService;
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

        return actorService.listActors(name);

    }

    @GetMapping("/{id}")
    public ActorResponse getActorById(@PathVariable Short id){
        return actorService.getActorById(id);
    }

    @PostMapping
    public ActorResponse createActor(@Validated(Create.class) @RequestBody ActorRequest data){
        return actorService.createActor(data);
    }

    @PatchMapping("/{id}")
    public ActorResponse updateActor(@Validated(Update.class)@PathVariable Short id, @RequestBody ActorRequest data){
        return actorService.updateActor(id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteActor(@PathVariable Short id){
        actorService.deleteActor(id);
        return ResponseEntity.ok("Actor with id: " + id + " has been successfully deleted");
    }
}
