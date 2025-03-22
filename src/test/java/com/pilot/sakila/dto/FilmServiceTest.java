package com.pilot.sakila.dto;

import com.pilot.sakila.entities.Actor;
import com.pilot.sakila.entities.Film;
import com.pilot.sakila.entities.Language;
import com.pilot.sakila.enums.Rating;
import com.pilot.sakila.repository.*;
import com.pilot.sakila.services.ActorService;
import com.pilot.sakila.services.FilmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
}
