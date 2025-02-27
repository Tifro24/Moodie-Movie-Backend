package com.pilot.sakila.services;

import com.pilot.sakila.dto.ValidationGroup;
import com.pilot.sakila.dto.request.ActorRequest;
import com.pilot.sakila.dto.response.ActorResponse;
import com.pilot.sakila.entities.Actor;
import com.pilot.sakila.repository.ActorRepository;
import com.pilot.sakila.repository.FilmActorRepository;
import com.pilot.sakila.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActorService {
        private final ActorRepository actorRepository;
        private final FilmRepository filmRepository;
        private final FilmActorRepository filmActorRepository;

        @Autowired
        public ActorService(ActorRepository actorRepository, FilmRepository filmRepository, FilmActorRepository filmActorRepository){
            this.actorRepository = actorRepository;
            this.filmRepository = filmRepository;
            this.filmActorRepository = filmActorRepository;
        }

    public List<Actor> listActors(Optional<String> name){

        return name
                .map(actorRepository::findByFullNameContainingIgnoreCase)
                .orElseGet(actorRepository::findAll);


    }

    public Actor getActorById(Short id){
        return actorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"This id does not exist within the database"));
    }

    public Actor createActor(String firstName, String lastName, List<Short> filmIds){
        final var actor = new Actor();
        actor.setFirstName(firstName);
        actor.setLastName(lastName);

        final var films = filmIds
                .stream()
                .map(filmId -> filmRepository
                        .findById(filmId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No film exists with that Id")))
                .toList();


        actor.setFilms(films);
        return actorRepository.save(actor);

    }

    public Actor updateActor(Short id, String firstName, String lastName, List<Short> filmIds){

        Actor actor = actorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor with id " + id + "not found"));

        if(firstName != null){
            actor.setFirstName(firstName);
        }

        if(lastName != null){
            actor.setLastName(lastName);
        }

        if(filmIds != null && !filmIds.isEmpty()){
            final var films = filmIds
                    .stream()
                    .map(filmId -> filmRepository
                            .findById(filmId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No film exists with that Id"))
                    ).toList();
            actor.setFilms(new ArrayList<>(films));
        }
        return actorRepository.save(actor);

    }

    public void deleteActor(@PathVariable Short id){

        if(!actorRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor not found");
        }
        filmActorRepository.deleteByActorId(id);
        actorRepository.deleteById(id);
    }
}
