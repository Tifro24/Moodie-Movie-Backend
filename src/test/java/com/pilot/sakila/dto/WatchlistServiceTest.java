package com.pilot.sakila.dto;
import static org.mockito.Mockito.*;

import com.pilot.sakila.entities.Film;
import com.pilot.sakila.entities.Language;
import com.pilot.sakila.entities.Preferences;
import com.pilot.sakila.entities.Watchlist;
import com.pilot.sakila.enums.Rating;
import com.pilot.sakila.repository.FilmRepository;
import com.pilot.sakila.repository.PreferencesRepository;
import com.pilot.sakila.repository.WatchlistRepository;
import com.pilot.sakila.services.WatchlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class WatchlistServiceTest {
    @Mock
    private WatchlistRepository watchlistRepository;

    @Mock
    private PreferencesRepository preferencesRepository;

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private WatchlistService service;

    @BeforeEach
    void setUp() {
        service = new WatchlistService(watchlistRepository, preferencesRepository, filmRepository);
    }

    static List<Watchlist> watchlists = List.of(
            new Watchlist((short) 1, "Watchlist 1", new Preferences((short) 1, "Happy", List.of(), Timestamp.valueOf(LocalDateTime.now())), List.of(), "ABC123"),
            new Watchlist((short) 2, "Watchlist 2", new Preferences((short) 1, "Happy", List.of(), Timestamp.valueOf(LocalDateTime.now())), List.of(), "ABC124"),
            new Watchlist((short) 3, "Watchlist 3", new Preferences((short) 1, "Happy", List.of(), Timestamp.valueOf(LocalDateTime.now())), List.of(), "ABC125"),
            new Watchlist((short) 4, "Watchlist 4", new Preferences((short) 1, "Happy", List.of(), Timestamp.valueOf(LocalDateTime.now())), List.of(), "ABC126"),
            new Watchlist((short) 5, "Watchlist 5", new Preferences((short) 1, "Happy", List.of(), Timestamp.valueOf(LocalDateTime.now())), List.of(), "ABC127")
    );

    static List<Film> films = java.util.List.of(
            new Film((short) 1, "Winner", "The biggest winner", Year.of(2002), new Language((byte) 1, "English", List.of()), null, (short) 150, Rating.PG, List.of(), List.of(), List.of()),
            new Film((short) 2, "The Winner 2", "The biggest of winners", Year.of(2003), new Language((byte) 1, "English", List.of()), null, (short) 160, Rating.PG_13, List.of(), List.of(), List.of()),
            new Film((short) 3, "Le Winner 3", "The biggest winner of winners", Year.of(2004), new Language((byte) 1, "English", List.of()), null, (short) 135, Rating.R, List.of(), List.of(), List.of()),
            new Film((short) 4, "Les Winners 4", "The bigger biggest winner of big winners", Year.of(2005), new Language((byte) 1, "English", List.of()), null, (short) 110, Rating.NC_17, List.of(), List.of(), List.of())
    );



    @Test
    public void testWatchlistCreation() {
        Watchlist expectedWatchlist = watchlists.getFirst();

        String sessionId = expectedWatchlist.getSessionId();
        String name = expectedWatchlist.getName();
        Short moodId = expectedWatchlist.getMood().getId();

        when(watchlistRepository.existsBySessionIdAndNameIgnoreCase(sessionId, name)).thenReturn(false);
        when(preferencesRepository.findById(moodId)).thenReturn(Optional.of(expectedWatchlist.getMood()));
        when(watchlistRepository.save(any(Watchlist.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Watchlist actualWatchlist = service.createWatchlist(sessionId, name, moodId);

        assertThat(actualWatchlist).isNotNull();
        assertThat(actualWatchlist.getSessionId()).isEqualTo(expectedWatchlist.getSessionId());
        assertThat(actualWatchlist.getName()).isEqualTo(expectedWatchlist.getName());
        assertThat(actualWatchlist.getMood()).isEqualTo(expectedWatchlist.getMood());
        assertThat(actualWatchlist.getFilms()).isEqualTo(expectedWatchlist.getFilms());

        verify(watchlistRepository).existsBySessionIdAndNameIgnoreCase(sessionId, name);
        verify(preferencesRepository).findById(moodId);

    }

    @Test
    public void createWatchlistThrowsErrorForInvalidMood() {
        String sessionId = "ABC123";
        String name = "Random";
        Short invalidMood = 999;

        when(watchlistRepository.existsBySessionIdAndNameIgnoreCase(sessionId, name)).thenReturn(false);
        when(preferencesRepository.findById(invalidMood)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createWatchlist(sessionId, name, invalidMood)).isInstanceOf(ResponseStatusException.class);

    }

    @Test
    public void createWatchlistThrowsErrorForNameAndSessionIdAlreadyExists() {
        String sessionId = "already";
        String name = "alreadyagain";
        Short moodId = 5;

        when(watchlistRepository.existsBySessionIdAndNameIgnoreCase(sessionId, name)).thenReturn(true);

        assertThatThrownBy(() -> service.createWatchlist(sessionId, name, moodId)).isInstanceOf(ResponseStatusException.class);

        verify(preferencesRepository, never()).findById(moodId);
    }

    @Test
    public void createWatchlistGeneratesRandomUUIDForBlankSessionId() {
        String name = "Blank Watchlist";
        Short moodId = 1;
        Preferences returnedMood = watchlists.get(2).getMood();


        when(watchlistRepository.existsBySessionIdAndNameIgnoreCase(any(), eq(name))).thenReturn(false);
        when(preferencesRepository.findById(moodId)).thenReturn(Optional.of(returnedMood));
        when(watchlistRepository.save(any(Watchlist.class))).thenAnswer(inv -> inv.getArgument(0));

        Watchlist actualWatchlist = service.createWatchlist("  ", name, moodId);

        assertThat(actualWatchlist.getSessionId()).isNotBlank();
        assertThat(actualWatchlist.getSessionId()).matches("^[a-f0-9\\-]{36}$");


    }

    @Test
    public void createWatchlistGeneratesRandomUUIDForNullSessionId() {
        String name = "Blank Watchlist";
        Short moodId = 1;
        Preferences returnedMood = watchlists.get(2).getMood();


        when(watchlistRepository.existsBySessionIdAndNameIgnoreCase(any(), eq(name))).thenReturn(false);
        when(preferencesRepository.findById(moodId)).thenReturn(Optional.of(returnedMood));
        when(watchlistRepository.save(any(Watchlist.class))).thenAnswer(inv -> inv.getArgument(0));

        Watchlist actualWatchlist = service.createWatchlist(null, name, moodId);

        assertThat(actualWatchlist).isNotNull();
        assertThat(actualWatchlist.getSessionId()).isNotBlank();
        assertThat(actualWatchlist.getSessionId()).isNotNull();
        assertThat(actualWatchlist.getSessionId()).matches("^[a-f0-9\\-]{36}$");

    }

    @Test
    public void addFilmToWatchlistAddsFilmToGivenWatchlist() {
        Watchlist exampleWatchlist = watchlists.get(1);
        Film exampleFilm = films.getFirst();

        exampleWatchlist.setFilms(new ArrayList<>());


        when(watchlistRepository.findById(exampleWatchlist.getId())).thenReturn(Optional.of(exampleWatchlist));
        when(filmRepository.findById(exampleFilm.getId())).thenReturn(Optional.of(exampleFilm));
        when(watchlistRepository.save(any(Watchlist.class))).thenAnswer(inv -> inv.getArgument(0));


        Watchlist updatedWatchlist = service.addFilmToWatchlist(exampleWatchlist.getId(), exampleFilm.getId());

        assertThat(updatedWatchlist.getFilms()).containsExactly(exampleFilm);

        verify(watchlistRepository).findById(exampleWatchlist.getId());
        verify(filmRepository).findById(exampleFilm.getId());

    }

    @Test
    public void addFilmToWatchlistThrowsErrorForNonExistentWatchlist() {
        Short invalidWatchlistId = 999;
        Short filmId = 3;

        when(watchlistRepository.findById(invalidWatchlistId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addFilmToWatchlist(invalidWatchlistId, filmId)).isInstanceOf(ResponseStatusException.class);

        verify(filmRepository, never()).findById(filmId);

    }

    @Test
    public void addFilmToWatchlistThrowsErrorForNonExistentFilm() {
        Watchlist exampleWatchlist = watchlists.get(3);
        Short invalidFilmId = 354;

        when(watchlistRepository.findById(exampleWatchlist.getId())).thenReturn(Optional.of(exampleWatchlist));
        when(filmRepository.findById(invalidFilmId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addFilmToWatchlist(exampleWatchlist.getId(), invalidFilmId)).isInstanceOf(ResponseStatusException.class);


    }

    @Test
    public void addFilmToWatchlistThrowsErrorForAlreadyExistentFilm() {
        Watchlist exampleWatchlist = watchlists.get(1);
        List<Film> existentFilmList = List.of(films.getFirst(), films.getLast());
        Film filmToAdd = films.getFirst();

        exampleWatchlist.setFilms(existentFilmList);


        when(watchlistRepository.findById(exampleWatchlist.getId())).thenReturn(Optional.of(exampleWatchlist));
        when(filmRepository.findById(filmToAdd.getId())).thenReturn(Optional.of(filmToAdd));


        assertThatThrownBy(() -> service.addFilmToWatchlist(exampleWatchlist.getId(), filmToAdd.getId())).isInstanceOf(ResponseStatusException.class);

        verify(watchlistRepository).findById(exampleWatchlist.getId());
        verify(filmRepository).findById(filmToAdd.getId());

    }

    @Test
    public void deleteWatchlistDeletesWatchlist() {
        Watchlist watchlistToDelete = watchlists.getLast();

        when(watchlistRepository.existsById(watchlistToDelete.getId())).thenReturn(true);

        service.deleteWatchlist(watchlistToDelete.getId());

        verify(watchlistRepository).deleteById(watchlistToDelete.getId());
    }

    @Test
    public void deleteWatchlistThrowsErrorForInvalidWatchlistI() {
        Short invalidWatchlistId = 999;

        when(watchlistRepository.existsById(invalidWatchlistId)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteWatchlist(invalidWatchlistId)).isInstanceOf(ResponseStatusException.class);

        verify(watchlistRepository, never()).deleteById(invalidWatchlistId);
    }

    @Test
    public void removeFilmFromWatchlistsRemovesFilmFromWatchlist() {
        Watchlist exampleWatchlist = watchlists.get(2);
        List<Film> existingFilms = new ArrayList<>(List.of(films.get(2), films.get(3))) ;

        Film filmToRemove = films.get(2);

        exampleWatchlist.setFilms(existingFilms);

        when(watchlistRepository.findById(exampleWatchlist.getId())).thenReturn(Optional.of(exampleWatchlist));
        when((filmRepository.findById(filmToRemove.getId()))).thenReturn(Optional.of(filmToRemove));
        when(watchlistRepository.save(any(Watchlist.class))).thenAnswer(inv -> inv.getArgument(0));

        Watchlist updatedWatchlist = service.removeFilmFromWatchlist(exampleWatchlist.getId(), filmToRemove.getId());

        assertThat(updatedWatchlist.getFilms()).containsExactly(films.get(3));

        verify(watchlistRepository).findById(exampleWatchlist.getId());
        verify(filmRepository).findById(filmToRemove.getId());
        verify(watchlistRepository).save(updatedWatchlist);

    }

    @Test
    public void removeFilmFromWatchlistThrowsErrorForInvalidWatchlist() {
        Short invalidWatchlistId = 999;
        Short filmId = 3;

        when(watchlistRepository.findById(invalidWatchlistId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.removeFilmFromWatchlist(invalidWatchlistId, filmId)).isInstanceOf(ResponseStatusException.class);

        verify(filmRepository, never()).findById(filmId);
    }

    @Test
    public void removeFilmFromWatchlistThrowsErrorForInvalidFilm() {
        Watchlist exampleWatchlist = watchlists.get(3);
        Short invalidFilmId = 354;

        when(watchlistRepository.findById(exampleWatchlist.getId())).thenReturn(Optional.of(exampleWatchlist));
        when(filmRepository.findById(invalidFilmId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.removeFilmFromWatchlist(exampleWatchlist.getId(), invalidFilmId)).isInstanceOf(ResponseStatusException.class);
    }
 }
