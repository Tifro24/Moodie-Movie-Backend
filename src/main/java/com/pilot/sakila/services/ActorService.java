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

    public List<ActorResponse> listActors(Optional<String> name){

        return name
                .map(actorRepository::findByFullNameContainingIgnoreCase)
                .orElseGet(actorRepository::findAll)
                .stream()
                .map(ActorResponse:: from)
                .toList();

    }

    public ActorResponse getActorById(Short id){
        return actorRepository.findById(id)
                .map(ActorResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"This id does not exist within the database"));
    }

    public ActorResponse createActor(ActorRequest data){
        final var actor = new Actor();
        actor.setFirstName(data.getFirstName());
        actor.setLastName(data.getLastName());

        final var films = data.getFilmIds()
                .stream()
                .map(filmId -> filmRepository
                        .findById(filmId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No film exists with that Id")))
                .toList();


        actor.setFilms(films);
        final var savedActor = actorRepository.save(actor);
        return ActorResponse.from(savedActor);
    }

    public ActorResponse updateActor(@Validated(ValidationGroup.Update.class)@PathVariable Short id, @RequestBody ActorRequest data){

        Actor actor = actorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor with id " + id + "not found"));

        if(data.getFirstName() != null){
            actor.setFirstName(data.getFirstName());
        }

        if(data.getLastName() != null){
            actor.setLastName(data.getLastName());
        }

        if(data.getFilmIds() != null && !data.getFilmIds().isEmpty()){
            final var films = data.getFilmIds()
                    .stream()
                    .map(filmId -> filmRepository
                            .findById(filmId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No film exists with that Id"))
                    ).toList();
            actor.setFilms(new ArrayList<>(films));
        }
        Actor updatedActor = actorRepository.save(actor);
        return ActorResponse.from(updatedActor);
    }

    public void deleteActor(@PathVariable Short id){

        if(!actorRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor not found");
        }
        filmActorRepository.deleteByActorId(id);
        actorRepository.deleteById(id);
    }
}
