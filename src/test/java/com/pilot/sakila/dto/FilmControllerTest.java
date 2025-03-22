package com.pilot.sakila.dto;


import com.pilot.sakila.controller.FilmController;
import com.pilot.sakila.dto.response.ActorResponse;
import com.pilot.sakila.dto.response.FilmResponse;
import com.pilot.sakila.entities.Actor;
import com.pilot.sakila.entities.Category;
import com.pilot.sakila.entities.Film;
import com.pilot.sakila.entities.Language;
import com.pilot.sakila.enums.Rating;
import com.pilot.sakila.services.FilmService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.Mockito.*;

public class FilmControllerTest {

    static FilmService service = mock(FilmService.class);
    static FilmController controller = new FilmController(service);

    static List<Film> films = List.of(
            new Film((short) 1, "Winner", "The biggest winner", Year.of(2002), new Language((byte) 1, "English", List.of()), null, (short) 150, Rating.PG, List.of(), List.of(), List.of()),
            new Film((short) 2, "The Winner 2", "The biggest of winners", Year.of(2003), new Language((byte) 1, "English", List.of()), null, (short) 160, Rating.PG_13, List.of(), List.of(), List.of()),
            new Film((short) 3, "Le Winner 3", "The biggest winner of winners", Year.of(2004), new Language((byte) 1, "English", List.of()), null, (short) 135, Rating.R, List.of(), List.of(), List.of()),
            new Film((short) 4, "Les Winners 4", "The bigger biggest winner of big winners", Year.of(2005), new Language((byte) 1, "English", List.of()), null, (short) 110, Rating.NC_17, List.of(), List.of(), List.of())


    );



    @BeforeAll
    public static void setup() {

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(service).getFilmById(anyShort());
        for(var film : films){
            doReturn(film).when(service).getFilmById(film.getId());
        }

    }

    @Test
    public void getFilmByIdReturnsFilmResponseForAValidFilmId(){
        Film expectedFilm = films.get(0);
        final var expectedResponse = FilmResponse.from(expectedFilm);

        doReturn(expectedFilm).when(service).getFilmById((short)1);

        FilmResponse actualResponse = controller.getFilmById((short) 1);
        assertNotNull(actualResponse);
        Assertions.assertEquals(expectedResponse.getId(), actualResponse.getId());
    }

    @Test
    public void getAllFilmsReturnFilmsOfFilmResponsesForStringContainedInName() {
        List<Film> expectedFilms = List.of(films.get(2), films.get(3));
        List<FilmResponse> expectedResponse = expectedFilms.stream().map(FilmResponse::from).toList();

        doReturn(expectedFilms).when(service).getAllFilms(Optional.of("Le"));

        List<FilmResponse> actualFilms = controller.getAllFilms(Optional.of("Le"));

        assertNotNull(actualFilms);
        assertEquals(expectedResponse.size(), actualFilms.size());

        assertThat(actualFilms).usingRecursiveComparison().isEqualTo(expectedResponse);


    }

    @Test
    public void getAllFilmsReturnsAllFilmsGivenEmptyOptional() {
        List<Film> expectedFilms = films;
        List<FilmResponse> expectedResponse = films.stream().map(FilmResponse::from).toList();
        doReturn(expectedFilms).when(service).getAllFilms(Optional.empty());

        List<FilmResponse> actualFilms = controller.getAllFilms(Optional.empty());

        assertNotNull(actualFilms);
        assertEquals(expectedResponse.size(), actualFilms.size());

        assertThat(actualFilms).usingRecursiveComparison().isEqualTo(expectedResponse);


    }

    @Test
    public void createFilmReturnsFilmResponseOfCreatedActor(){
        Film filmToBeCreated = new Film((short) 5,
                "Da Real Crusher",
                "A story about the realest crate crusher",
                Year.of(2015),
                new Language((byte) 1, "English", List.of()),
                null,
                (short) 100,
                Rating.PG_13,
                List.of(),
                List.of(),
                List.of());

        FilmResponse expectedCreatedFilm = FilmResponse.from((filmToBeCreated));
        doReturn(filmToBeCreated).when(service).createFilm(anyString(), anyString(), anyShort(), anyShort(), anyShort(), any(Rating.class), anyList(),anyList());

        FilmResponse actualCreatedFilm = controller.createFilm("Da Real Crusher",
                "A story about the realest crate crusher",
                (short) 2015,
                (short) 1,
                (short) 100,
                Rating.PG_13,
                List.of(),
                List.of());

        assertThat(actualCreatedFilm).isNotNull();
        assertThat(actualCreatedFilm).usingRecursiveComparison().isEqualTo(expectedCreatedFilm);

    }

    @Test
    public void updatedFilmReturnsFilmResponseForUpdatedFilm(){
        Film filmToUpdate = films.get(2);


        filmToUpdate.setTitle("La Winner 2");


        FilmResponse expectedUpdatedFilm = FilmResponse.from(filmToUpdate);

        doReturn(filmToUpdate).when(service).updateFilm(any(), any(), any(), any(), any(), any(), any(), any(), any());

        FilmResponse actualUpdatedFilm = controller.updateFilm(filmToUpdate.getId(), "La Winner 2",null, null, null, null, null, null, null);

        assertThat(actualUpdatedFilm).isNotNull();
        assertThat(actualUpdatedFilm).usingRecursiveComparison().isEqualTo(expectedUpdatedFilm);
    }

    @Test
    public void deletedFilmReturnsSuccessMessageForDeletedFilm(){

        Short filmToDeleteId = (short)2;
        ResponseEntity<String> expectedMessage = ResponseEntity.ok("Film with id: " + filmToDeleteId + " has been successfully deleted");;


        ResponseEntity<String> actualMessage = controller.deleteFilm(filmToDeleteId);

        assertNotNull(actualMessage);
        Assertions.assertEquals(expectedMessage.getStatusCode(), actualMessage.getStatusCode());
        Assertions.assertEquals(expectedMessage, actualMessage);

    }


    @Test
    public void getMoviesByMoodReturnsFilmResponsesCorrespondingMood(){
        String mood = "happy";
        List<String> mockGenres = List.of("Comedy", "Family");
        List<Film> mockFilms = List.of(films.get(1), films.get(2));
        List<FilmResponse> expectedResponses = mockFilms.stream().map(FilmResponse::from).toList();

        doReturn(mockGenres).when(service).mapGenresToMood(mood);
        doReturn(mockFilms).when(service).getFilmsByGenres(mockGenres);

        List<FilmResponse> actualResponses = controller.getMoviesByMood(mood);

        assertNotNull(actualResponses);
        assertEquals(expectedResponses.size(), actualResponses.size());
        assertThat(actualResponses).usingRecursiveComparison().isEqualTo(expectedResponses);
    }

    @Test
    public void getMoviesByGenreReturnsFilmResponsesCorrespondingGenre() {
        String mockGenre = "Action";
        List<Film> mockFilms = List.of(films.get(0), films.get(3));
        List<FilmResponse> expectedFilms = mockFilms.stream().map(FilmResponse::from).toList();

        doReturn(mockFilms).when(service).getFilmsByCategoryName(mockGenre);

        List<FilmResponse> actualFilms = controller.getMoviesByGenre(mockGenre);

        assertNotNull(actualFilms);
        assertEquals(expectedFilms.size(), actualFilms.size());
        assertThat(actualFilms).usingRecursiveComparison().isEqualTo(expectedFilms);
    }

}
