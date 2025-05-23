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
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.in;
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
            new Film((short) 1, "Winner", "The biggest winner", Year.of(2002), new Language((byte) 1, "English", List.of()), null, (short) 150, Rating.PG, List.of(), List.of(), List.of()),
            new Film((short) 2, "The Winner 2", "The biggest of winners", Year.of(2003), new Language((byte) 1, "English", List.of()), null, (short) 160, Rating.PG_13, List.of(), List.of(), List.of()),
            new Film((short) 3, "Le Winner 3", "The biggest winner of winners", Year.of(2004), new Language((byte) 1, "English", List.of()), null, (short) 135, Rating.R, List.of(), List.of(), List.of()),
            new Film((short) 4, "Les Winners 4", "The bigger biggest winner of big winners", Year.of(2005), new Language((byte) 1, "English", List.of()), null, (short) 110, Rating.NC_17, List.of(), List.of(), List.of())
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

        // Given
        Short actorId = 3;
        String updatedFirstName = "Benji";
        String updatedLastName = "Okonkwo";
        List<Short> filmIds = List.of((short) 1, (short) 2);

        // Create a modifiable copy of the actor to simulate existing one
        Actor existingActor = new Actor(actorId, "Charles", "Owusu", "Charles Owusu", List.of());

        // Create the updated actor we expect to get back
        Actor expectedUpdatedActor = new Actor();
        expectedUpdatedActor.setId(actorId);
        expectedUpdatedActor.setFirstName(updatedFirstName);
        expectedUpdatedActor.setLastName(updatedLastName);
        expectedUpdatedActor.setFilms(List.of(films.get(0), films.get(1)));

        // Mocks
        when(actorRepository.findById(actorId)).thenReturn(Optional.of(existingActor));
        when(filmRepository.findById((short) 1)).thenReturn(Optional.of(films.get(0)));
        when(filmRepository.findById((short) 2)).thenReturn(Optional.of(films.get(1)));
        when(actorRepository.save(any(Actor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Actor actualUpdatedActor = service.updateActor(actorId, updatedFirstName, updatedLastName, filmIds);

        // Then
        assertThat(actualUpdatedActor).isNotNull();
        assertThat(actualUpdatedActor.getId()).isEqualTo(actorId);
        assertThat(actualUpdatedActor.getFirstName()).isEqualTo(updatedFirstName);
        assertThat(actualUpdatedActor.getLastName()).isEqualTo(updatedLastName);
        assertThat(actualUpdatedActor.getFilms()).usingRecursiveComparison().isEqualTo(List.of(films.get(0), films.get(1)));



    }

    @Test
    public void updateActorOnlyUpdatesFilmsWhenGivenOnlyFilms() {
        Actor actorToUpdate = actors.get(4);

        List<Film> expectedFilms = List.of(films.get(2), films.get(3));

        when(actorRepository.findById(actorToUpdate.getId())).thenReturn(Optional.of(actorToUpdate));
        when(filmRepository.findById(films.get(2).getId())).thenReturn(Optional.of(films.get(2)));
        when(filmRepository.findById(films.get(3).getId())).thenReturn(Optional.of(films.get(3)));
        when(actorRepository.save(any(Actor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Actor updatedActor = service.updateActor(actorToUpdate.getId(), null, null, List.of(films.get(2).getId(), (films.get(3).getId())));

        assertThat(updatedActor).isNotNull();
        assertThat(updatedActor.getId()).isEqualTo(actorToUpdate.getId());
        assertThat(updatedActor.getFirstName()).isEqualTo(actorToUpdate.getFirstName());
        assertThat(updatedActor.getLastName()).isEqualTo(actorToUpdate.getLastName());
        assertThat(updatedActor.getFilms()).usingRecursiveComparison().isEqualTo(expectedFilms);

    }

    @Test
    public void updateActorOnlyUpdatesNamesWhenOnlyNameGives() {
        Actor actorToUpdate = actors.get(2);

        String newFirstName = "John";

        when(actorRepository.findById(actorToUpdate.getId())).thenReturn(Optional.of(actorToUpdate));
        when(actorRepository.save(any(Actor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Actor updatedActor = service.updateActor(actorToUpdate.getId(), newFirstName, null, List.of());

        assertThat(updatedActor).isNotNull();
        assertThat(updatedActor.getId()).isEqualTo(actorToUpdate.getId());
        assertThat(updatedActor.getFirstName()).isEqualTo(newFirstName);
        assertThat(updatedActor.getLastName()).isEqualTo(actorToUpdate.getLastName());
        assertThat(updatedActor.getFilms()).usingRecursiveComparison().isEqualTo(actorToUpdate.getFilms());


    }

    @Test
    public void updateActorsThrowsErrorForNonexistentFilmIds() {
        Actor actorToUpdate = actors.get(3);

        Short nonExistentFilmId = 856;

        when(actorRepository.findById(actorToUpdate.getId())).thenReturn(Optional.of(actorToUpdate));
        when(filmRepository.findById(nonExistentFilmId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            service.updateActor(actorToUpdate.getId(), null, null, List.of(nonExistentFilmId));
        });

        verify(actorRepository).findById(actorToUpdate.getId());
        verify(filmRepository).findById(nonExistentFilmId);


    }

    @Test
    public void createActorThrowsErrorForNonexistentFilmIds() {
        Short nonExistentFilmId = 856;
        String mockFirstName = "Gbenga";
        String mockLastName = "Kitombo";

        when(filmRepository.findById(nonExistentFilmId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            service.createActor(mockFirstName, mockLastName,  List.of(nonExistentFilmId));
        });

        verify(filmRepository).findById(nonExistentFilmId);
    }

    @Test
    public void deleteActorDeletesActorForValidId() {
        Actor actorToDelete = actors.get(0);
        when(actorRepository.existsById(actorToDelete.getId())).thenReturn(true);
        service.deleteActor(actorToDelete.getId());
        verify(actorRepository).deleteById(actorToDelete.getId());
        verify(filmActorRepository).deleteByActorId(actorToDelete.getId());
    }

    @Test
    public void updateActorThrowsErrorForNonexistentActor() {
        Short nonExistentId = 784;

        when(actorRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->  {
            service.updateActor(nonExistentId, null, null, null);
        });

        verify(actorRepository).findById(nonExistentId);
    }

    @Test
    public void deleteActorThrowsErrorForActorThatDoesntExist() {
        Short nonExistentId = 99;

        when(actorRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> {
            service.deleteActor(nonExistentId);
        });

        verify(actorRepository, never()).deleteById(nonExistentId);
    }

    @Test
    public void getActorByIdReturnsActorGivenValidId() {
        Actor validActor = actors.get(3);

        when(actorRepository.findById(validActor.getId())).thenReturn(Optional.of(validActor));

        Actor returnedActor = service.getActorById(validActor.getId());

        assertNotNull(returnedActor);
        assertThat(returnedActor.getId()).isEqualTo(validActor.getId());
        assertThat(returnedActor.getFirstName()).isEqualTo(validActor.getFirstName());
        assertThat(returnedActor.getLastName()).isEqualTo(validActor.getLastName());
        assertThat(returnedActor.getFilms()).usingRecursiveComparison().isEqualTo(validActor.getFilms());
        verify(actorRepository).findById(validActor.getId());
    }

    @Test
    public void getActorByIdThrowsErrorForInvalidId() {
        Short invalidId = 999;

        when(actorRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            service.getActorById(invalidId);
        });

        verify(actorRepository).findById(invalidId);
    }






}
