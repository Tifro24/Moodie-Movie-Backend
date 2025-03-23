package com.pilot.sakila.dto;

import com.pilot.sakila.dto.response.PartialActorResponse;
import com.pilot.sakila.entities.Actor;
import com.pilot.sakila.entities.Category;
import com.pilot.sakila.entities.Film;
import com.pilot.sakila.entities.Language;
import com.pilot.sakila.enums.Rating;
import com.pilot.sakila.repository.*;
import com.pilot.sakila.services.ActorService;
import com.pilot.sakila.services.FilmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilmServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private FilmActorRepository filmActorRepository;

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private FilmTextRepository filmTextRepository;

    @InjectMocks
    private FilmService service;

    @BeforeEach
    void setUp() {
        service = new FilmService( filmRepository, actorRepository, languageRepository, filmActorRepository, categoryRepository, filmTextRepository);
    }

    static List<Film> films = java.util.List.of(
            new Film((short) 1, "Winner", "The biggest winner", Year.of(2002), new Language((byte) 1, "English", List.of()), null, (short) 150, Rating.PG, List.of(), List.of(), List.of()),
            new Film((short) 2, "The Winner 2", "The biggest of winners", Year.of(2003), new Language((byte) 1, "English", List.of()), null, (short) 160, Rating.PG_13, List.of(), List.of(), List.of()),
            new Film((short) 3, "Le Winner 3", "The biggest winner of winners", Year.of(2004), new Language((byte) 1, "English", List.of()), null, (short) 135, Rating.R, List.of(), List.of(), List.of()),
            new Film((short) 4, "Les Winners 4", "The bigger biggest winner of big winners", Year.of(2005), new Language((byte) 1, "English", List.of()), null, (short) 110, Rating.NC_17, List.of(), List.of(), List.of())
            );

    static List<Actor> actors = List.of(
            new Actor((short) 1, "Elijah", "Duma", "Elijah Duma", List.of()),
            new Actor((short) 2, "Samuel", "Ojo", "Samuel Ojo", List.of()),
            new Actor((short) 3, "Charles", "Owusu", "Charles Owusu", List.of()),
            new Actor((short) 4, "Goku", "Kakaroto", "Goku Kakaroto", List.of()),
            new Actor((short) 5, "Isagi", "Yoichi", "Isagi Yoichi", List.of())

    );


    @Test
    public void updateFilmReturnsAnUpdatedFilm() {
        Short filmId = 2;
        String newTitle = "Like Mike";
        String newDescription = "Like the Mikiest Mike ";

        Film existingFilm =  new Film(filmId, "Le Winner 3", "The biggest winner of winners", Year.of(2004), new Language((byte) 1, "English", List.of()), null, (short) 135, Rating.R, List.of(), List.of(), List.of());

        Language mockLanguage = new Language((byte) 1, "English", List.of());

        // Mocks
        when(filmRepository.findById(filmId)).thenReturn(Optional.of(existingFilm));
        when(languageRepository.findById((short) mockLanguage.getId())).thenReturn(Optional.of(mockLanguage));
        when(filmRepository.save(any(Film.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Film actualUpdatedFilm = service.updateFilm(
                filmId,
                newTitle,
                newDescription,
                (short) 2004,
                (short)mockLanguage.getId(),
                existingFilm.getLength(),
                existingFilm.getRating(),
                List.of(),
                List.of()
        );

        // Then
        assertThat(actualUpdatedFilm).isNotNull();
        assertThat(actualUpdatedFilm.getId()).isEqualTo(filmId);
        assertThat(actualUpdatedFilm.getTitle()).isEqualTo(newTitle);
        assertThat(actualUpdatedFilm.getDescription()).isEqualTo(newDescription);
        assertThat(actualUpdatedFilm.getReleaseYear()).isEqualTo(existingFilm.getReleaseYear());
        assertThat(actualUpdatedFilm.getLength()).isEqualTo(existingFilm.getLength());
        assertThat(actualUpdatedFilm.getRating()).isEqualTo(existingFilm.getRating());
        assertThat(actualUpdatedFilm.getLanguage()).isEqualTo(mockLanguage);


    }

    @Test
    public void updateActorThrowsErrorForNonexistentFilmId() {
        Short falseId = 99;

        when(filmRepository.findById(falseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            service.updateFilm(falseId,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null))
                .isInstanceOf(ResponseStatusException.class);

        verify(filmRepository).findById(falseId);
    }

    @Test
    public void updateFilmThrowsErrorForNonexistentLanguageId() {
        Short falseId = 99;
        Film exampleFilm = films.get(2);

        when(filmRepository.findById(exampleFilm.getId())).thenReturn(Optional.of(exampleFilm));
        when(languageRepository.findById(falseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            service.updateFilm(exampleFilm.getId(),
                    "JunkRat",
                    null,
                    null,
                    falseId,
                    null
                    ,null,
                    List.of(),
                    List.of())).isInstanceOf(ResponseStatusException.class);

        verify(filmRepository).findById(exampleFilm.getId());
        verify(languageRepository).findById(falseId);
    }

    @Test
    public void updateFilmCorrectlySetsCategoriesAndCastToFilm() {
        List<Short> exampleCategoryIds = List.of((short) 24,(short) 17,(short) 93);
        List<Actor> currentActors = List.of(actors.get(0), actors.get(1));

        Film exampleFilm = films.get(3);
        exampleFilm.setCast(currentActors);

        List<Actor> newActors = List.of(actors.get(2), actors.get(3));

        when(filmRepository.findById(exampleFilm.getId())).thenReturn(Optional.of(exampleFilm));

        for (Short id: exampleCategoryIds) {
            when(categoryRepository.findById(id)).thenReturn(Optional.of(new Category(id, "Category - "+id, List.of(), List.of())));
        }
        when(actorRepository.findById(newActors.getFirst().getId())).thenReturn(Optional.of(newActors.getFirst()));
        when(actorRepository.findById(newActors.getLast().getId())).thenReturn(Optional.of(newActors.getLast()));

        when(actorRepository.findById(currentActors.getFirst().getId())).thenReturn(Optional.of(currentActors.getFirst()));
        when(actorRepository.findById(currentActors.getLast().getId())).thenReturn(Optional.of(currentActors.getLast()));
        when(filmRepository.save(any(Film.class))).thenAnswer(inv -> inv.getArgument(0));

        Film updatedFilm = service.updateFilm(
                exampleFilm.getId(),
                null,
                null,
                null,
                null,
                null,
                null,
                exampleCategoryIds,
                List.of(newActors.getFirst().getId(), newActors.getLast().getId()));

        List<Short> updatedCastIds = updatedFilm.getCast()
                .stream()
                .map(PartialActorResponse::getId)
                .sorted()
                .toList();

        List<Short> updatedCategoryIds = updatedFilm.getCategories()
                        .stream().map(Category::getId)
                        .toList();

        assertThat(updatedFilm).isNotNull();
        assertThat(updatedFilm.getId()).isEqualTo(exampleFilm.getId());
        assertThat(updatedCategoryIds).usingRecursiveComparison().isEqualTo(exampleCategoryIds);
        assertThat(updatedCastIds).isEqualTo(List.of(currentActors.getFirst().getId(),
                currentActors.getLast().getId(),
                newActors.getFirst().getId(),
                newActors.getLast().getId()));

    }

    private static Stream<Arguments> getMoodsAndExpectedGenres() {
        return Stream.of(
                Arguments.of("happy", List.of("Comedy", "Family")),
                Arguments.of("sad", List.of("Drama", "Romance")),
                Arguments.of("adventurous", List.of("Action", "Adventure")),
                Arguments.of("romantic", List.of("Romance", "Drama")),
                Arguments.of("funny", List.of("Comedy")),
                Arguments.of("excited", List.of("Action", "Adventure")),
                Arguments.of("scary", List.of("Horror", "Thriller")),
                Arguments.of("unknown", List.of())

        );
    }

 @ParameterizedTest
 @MethodSource("getMoodsAndExpectedGenres")
 public void mapGenresToMoodReturnsExpectedGenres(String mood, List<String> expectedGenres) {
    List<String> actualGenres = service.mapGenresToMood(mood);

    assertThat(actualGenres).containsExactlyInAnyOrderElementsOf(expectedGenres);

 }

 @Test
 public void createFilmReturnsACreatedFilm() {
     Film expectedFilm = films.get(2);
     List<Category> categoryList = List.of(
             new Category((short) 1, "Action" , List.of(), List.of()),
             new Category((short) 2, "Adventure" , List.of(), List.of()));
     List<Short> categoryIds = List.of((short) 1, (short) 2);
     List<Actor> exampleActors = List.of(actors.get(0), actors.get(1));
     List<Short> actorIds = List.of((short) 1, (short) 2);

     expectedFilm.setCategories(categoryList);
     expectedFilm.setCast(exampleActors);

     when(languageRepository.findById(Short.valueOf(expectedFilm.getLanguage().getId()))).thenReturn(Optional.ofNullable(expectedFilm.getLanguage()));
     when(categoryRepository.findAllById(categoryIds)).thenReturn(expectedFilm.getCategories());

     for (Actor exampleActor :  exampleActors) {
         when(actorRepository.findById(exampleActor.getId())).thenReturn(Optional.of(exampleActor));
     }
     when(filmRepository.save(any(Film.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

     Film actualFilm = service.createFilm(
             expectedFilm.getTitle(),
             expectedFilm.getDescription(),
             (short) 2004,
             Short.valueOf(expectedFilm.getLanguage().getId()),
             expectedFilm.getLength(),
             expectedFilm.getRating(),
             categoryIds,
             actorIds);

     assertThat(actualFilm).isNotNull();
     assertThat(actualFilm.getTitle()).isEqualTo(expectedFilm.getTitle());
     assertThat(actualFilm.getDescription()).isEqualTo(expectedFilm.getDescription());
     assertThat(actualFilm.getReleaseYear()).isEqualTo(expectedFilm.getReleaseYear());
     assertThat(actualFilm.getLanguage()).isEqualTo(expectedFilm.getLanguage());
     assertThat(actualFilm.getLength()).isEqualTo(expectedFilm.getLength());
     assertThat(actualFilm.getRating()).isEqualTo(expectedFilm.getRating());
     assertThat(actualFilm.getCategories()).isEqualTo(expectedFilm.getCategories());
     assertThat(actualFilm.getCast())
             .extracting(PartialActorResponse::getId)
             .containsExactlyInAnyOrderElementsOf(
                     expectedFilm.getCast().stream()
                             .map(PartialActorResponse::getId)
                             .toList());

     verify(languageRepository).findById(Short.valueOf(expectedFilm.getLanguage().getId()));
     verify(categoryRepository).findAllById(categoryIds);
     verify(actorRepository).findById(exampleActors.getFirst().getId());
     verify(actorRepository).findById(exampleActors.getLast().getId());
 }

 @Test
 public void deleteFilmDeletesFilm() {
    Film filmToDelete = new Film((short) 55, "Les Winners 5", "The bigger biggest winner of big winners", Year.of(2005), new Language((byte) 1, "English", List.of()), null, (short) 110, Rating.NC_17, List.of(), List.of(), List.of());

    when(filmRepository.existsById(filmToDelete.getId())).thenReturn(true);

    service.deleteFilm(filmToDelete.getId());

    verify(filmRepository).deleteById(filmToDelete.getId());
    verify(filmTextRepository).deleteByFilmId(filmToDelete.getId());
    verify(filmActorRepository).deleteByFilmId(filmToDelete.getId());
 }

 @Test
 public void deleteFilmThrowsErrorForInvalidId() {
    Short invalidId = 99;

    when(filmRepository.existsById(invalidId)).thenReturn(false);

    assertThatThrownBy(() -> service.deleteFilm(invalidId)).isInstanceOf(ResponseStatusException.class);

    verify(filmTextRepository, never()).deleteByFilmId(invalidId);
    verify(filmActorRepository, never()).deleteByFilmId(invalidId);
    verify(filmRepository, never()).deleteById(invalidId);
 }

 @Test
 public void getAllFilmsReturnsRelevantFilmsGivenTitle() {
    String userInput = "Le";

    List<Film> expectedFilms = List.of(films.get(2), films.get(3));

    when(filmRepository.findByTitleContainingIgnoreCase(userInput)).thenReturn(expectedFilms);

    List<Film> actualFilms = service.getAllFilms(Optional.of(userInput));

    assertThat(actualFilms).isNotNull();
    assertThat(actualFilms).containsExactlyInAnyOrderElementsOf(expectedFilms);
 }

 @Test
 public void getAllFilmsReturnsAllFilmsForNoGivenTitle() {
    List<Film> expectedFilms = films;

    when(filmRepository.findAll()).thenReturn(expectedFilms);

    List<Film> actualFilms = service.getAllFilms(Optional.empty());

     assertThat(actualFilms).isNotNull();
     assertThat(actualFilms).containsExactlyInAnyOrderElementsOf(expectedFilms);


 }


}
