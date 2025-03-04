package com.pilot.sakila.dto;

import com.pilot.sakila.entities.Actor;
import com.pilot.sakila.entities.Film;
import com.pilot.sakila.entities.Language;
import com.pilot.sakila.enums.Rating;
import com.pilot.sakila.repository.ActorRepository;
import com.pilot.sakila.repository.FilmActorRepository;
import com.pilot.sakila.repository.FilmRepository;
import com.pilot.sakila.services.ActorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ActorServiceTest {
    @Mock
    private ActorRepository actorRepository;

    @Mock
    private FilmActorRepository filmActorRepository;

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private ActorService service;

    @BeforeEach
    void setUp() {
        service = new ActorService(actorRepository, filmRepository, filmActorRepository);
    }
    static List<Actor> actors = List.of(
            new Actor((short) 1, "Elijah", "Duma", "Elijah Duma", List.of()),
            new Actor((short) 2, "Samuel", "Ojo", "Samuel Ojo", List.of()),
            new Actor((short) 3, "Charles", "Owusu", "Charles Owusu", List.of()),
            new Actor((short) 4, "Goku", "Kakaroto", "Goku Kakaroto", List.of()),
            new Actor((short) 5, "Isagi", "Yoichi", "Isagi Yoichi", List.of())

    );

    static List<Film> films = List.of(
            new Film((short) 1, "Winner", "The biggest winner", Year.of(2002), new Language((byte) 1, "English", List.of()), null, (short) 150, Rating.PG, List.of(), List.of()),
            new Film((short) 2, "The Winner 2", "The biggest of winners", Year.of(2003), new Language((byte) 1, "English", List.of()), null, (short) 160, Rating.PG_13, List.of(), List.of()),
            new Film((short) 3, "Le Winner 3", "The biggest winner of winners", Year.of(2004), new Language((byte) 1, "English", List.of()), null, (short) 135, Rating.R, List.of(), List.of()),
            new Film((short) 4, "Les Winners 4", "The bigger biggest winner of big winners", Year.of(2005), new Language((byte) 1, "English", List.of()), null, (short) 110, Rating.NC_17, List.of(), List.of())


    );




    @Test
    public void listActorsReturnsActorNamesWhenStringContainedInSaidActorsNames(){
        List<Actor> expectedActors = List.of(actors.get(0), actors.get(1));

        when(actorRepository.findByFullNameContainingIgnoreCase("El")).thenReturn(expectedActors);

        List<Actor> actualActors = service.listActors(Optional.of("El"));

        assertThat(actualActors).isNotNull();

        assertThat(actualActors).isEqualTo(expectedActors);


    }

    @Test
    public void listActorsReturnsListOfActorsWhenNoGivenString(){
        List<Actor> expectedActors = actors;

        when(actorRepository.findAll()).thenReturn(expectedActors);

        List<Actor> actualActors = service.listActors(Optional.empty());

        assertThat(actualActors).isNotNull();

        assertThat(actualActors).isEqualTo(expectedActors);
    }

    @Test
    public void createActorReturnsActor(){

        List<Short> filmIds = List.of((short) 1, (short) 2);



        Actor expectedActor = new Actor();
        expectedActor.setFirstName("Bukayo");
        expectedActor.setLastName("Saka");
        expectedActor.setFilms(List.of(films.get(0), films.get(1)));

        when(filmRepository.findById((short) 1)).thenReturn(Optional.of(films.get(0)));
        when(filmRepository.findById((short) 2)).thenReturn(Optional.of(films.get(1)));

        when(actorRepository.save(argThat(actor ->
                actor.getFirstName().equals("Bukayo") &&
                        actor.getLastName().equals("Saka")
        ))).thenReturn(expectedActor);

        Actor actualActor = service.createActor("Bukayo", "Saka", filmIds);

        assertThat(actualActor).isNotNull();
        assertThat(actualActor).usingRecursiveComparison().isEqualTo(expectedActor);


    }

    @Test
    public void updateActorReturnsUpdatedActor(){

        Actor updatedActor = actors.get(3);
        updatedActor.setFirstName("Benji");

        
    }



}
