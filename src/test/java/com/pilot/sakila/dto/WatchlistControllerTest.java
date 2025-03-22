package com.pilot.sakila.dto;

import com.pilot.sakila.controller.WatchlistController;
import com.pilot.sakila.dto.response.MiniFilmResponse;
import com.pilot.sakila.dto.response.WatchlistResponse;
import com.pilot.sakila.entities.Film;
import com.pilot.sakila.entities.Language;
import com.pilot.sakila.entities.Preferences;
import com.pilot.sakila.entities.Watchlist;
import com.pilot.sakila.enums.Rating;
import com.pilot.sakila.services.FilmService;
import com.pilot.sakila.services.WatchlistService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.Mockito.*;

public class WatchlistControllerTest {

    static WatchlistService wService = mock(WatchlistService.class);
    static FilmService fService = mock(FilmService.class);
    static WatchlistController controller = new WatchlistController(wService, fService);

    static List<Watchlist> watchlists =  List.of(
            new Watchlist((short) 1, "Greatest Hits", new Preferences((short) 1, "Happy", List.of(), new Timestamp(System.currentTimeMillis())), List.of(), "ABC123"),
            new Watchlist((short) 2, "Greatest Hits 2", new Preferences((short) 1, "Happy", List.of(), new Timestamp(System.currentTimeMillis())), List.of(), "ABC124"),
            new Watchlist((short) 3, "Greatest Hits 3", new Preferences((short) 1, "Happy", List.of(), new Timestamp(System.currentTimeMillis())), List.of(), "ABC125"),
            new Watchlist((short) 4, "Greatest Hits 4", new Preferences((short) 1, "Happy", List.of(), new Timestamp(System.currentTimeMillis())), List.of(), "ABC126"),
            new Watchlist((short) 5, "Greatest Hits 5", new Preferences((short) 1, "Happy", List.of(), new Timestamp(System.currentTimeMillis())), List.of(), "ABC127")
    );

    static List<Film> films = List.of(
            new Film((short) 1, "Winner", "The biggest winner", Year.of(2002), new Language((byte) 1, "English", List.of()), null, (short) 150, Rating.PG, List.of(), List.of(), List.of()),
            new Film((short) 2, "The Winner 2", "The biggest of winners", Year.of(2003), new Language((byte) 1, "English", List.of()), null, (short) 160, Rating.PG_13, List.of(), List.of(), List.of()),
            new Film((short) 3, "Le Winner 3", "The biggest winner of winners", Year.of(2004), new Language((byte) 1, "English", List.of()), null, (short) 135, Rating.R, List.of(), List.of(), List.of()),
            new Film((short) 4, "Les Winners 4", "The bigger biggest winner of big winners", Year.of(2005), new Language((byte) 1, "English", List.of()), null, (short) 110, Rating.NC_17, List.of(), List.of(), List.of())


    );

    @BeforeAll
    public static void setup() {

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(fService).getFilmById(anyShort());
        for(var film : films){
            doReturn(film).when(fService).getFilmById(film.getId());
        }

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(wService).getUserWatchlists(anyString());
        for(var watchlist : watchlists){
            doReturn(watchlists).when(wService).getUserWatchlists(watchlist.getSessionId());
        }

    }

    @Test
    public void getAllWatchlistsReturnsListOfWatchlistResponses() {
        List<Watchlist> mockWatchlist = watchlists;
        List<WatchlistResponse> expectedWatchlist = mockWatchlist.stream().map(WatchlistResponse::from).toList();

        doReturn(mockWatchlist).when(wService).getAllWatchlists();

        List<WatchlistResponse> actualWatchlist = controller.getAllWatchlists();

        assertNotNull(actualWatchlist);
        assertEquals(expectedWatchlist.size(), actualWatchlist.size());
        assertThat(actualWatchlist).usingRecursiveComparison().isEqualTo(expectedWatchlist);
    }

    @Test
    public void getWatchlistsByMoodReturnsListOfWatchlistResponsesForGivenMood() {
        Short moodId = 1;
        List<Watchlist> mockWatchlists = List.of(watchlists.get(0), watchlists.get(1));
        List<WatchlistResponse> expectedWatchlists = mockWatchlists.stream().map(WatchlistResponse::from).toList();

        doReturn(mockWatchlists).when(wService).getWatchlistsByMood(moodId);

        List<WatchlistResponse> actualWatchlists = controller.getWatchlistsByMood(moodId);

        assertNotNull(actualWatchlists);
        assertEquals(actualWatchlists.size(), expectedWatchlists.size());
        assertThat(actualWatchlists).usingRecursiveComparison().isEqualTo(expectedWatchlists);
    }

    @Test
    public void getUserWatchlistsByMoodReturnsListOfWatchlistResponsesForGivenSessionId() {
        String sessionId = "ABC123";
        List<Watchlist> mockWatchlists = List.of(watchlists.get(0), watchlists.get(1));
        List<WatchlistResponse> expectedWatchlists = mockWatchlists.stream().map(WatchlistResponse::from).toList();

        doReturn(mockWatchlists).when(wService).getUserWatchlists(sessionId);

        List<WatchlistResponse> actualWatchlists = controller.getUserWatchlists(sessionId);

        assertNotNull(actualWatchlists);
        assertEquals(actualWatchlists.size(), expectedWatchlists.size());
        assertThat(actualWatchlists).usingRecursiveComparison().isEqualTo(expectedWatchlists);
    }

    @Test
    public void createWatchlistReturnsAWatchlistResponseForCreatedWatchlist() {
        Watchlist watchlistToBeCreated = new Watchlist((short) 6,
                "Greatest Hits 6",
                new Preferences((short) 1, "Happy", List.of(), new Timestamp(System.currentTimeMillis())),
                List.of(),
                "ABC127");

        WatchlistResponse expectedWatchlist = WatchlistResponse.from(watchlistToBeCreated);

        doReturn(watchlistToBeCreated).when(wService).createWatchlist(anyString(), anyString(), anyShort());

        WatchlistResponse actualWatchlist = controller.createWatchlist("ABC127", "Greatest Hits 6", (short) 6);

        assertThat(actualWatchlist).usingRecursiveComparison().isEqualTo(expectedWatchlist);
    }

    @Test
    public void searchFilmsReturnsListOfMiniFilmResponseGivenTitle() {
        String title = "Greatest";
        List<Film> mockedFilms = films;
        List<MiniFilmResponse> expectedFilms = mockedFilms.stream().map(MiniFilmResponse::from).toList();

        doReturn(mockedFilms).when(fService).getAllFilms(Optional.of(title));
        System.out.println("🎬 Mocked Films in Test: " + mockedFilms);

        List<MiniFilmResponse> actualFilms = controller.searchFilms(Optional.of(title));

        System.out.println("Expected: " + expectedFilms);
        System.out.println("Actual: " + actualFilms);

        assertNotNull(actualFilms);
        assertThat(actualFilms).usingRecursiveComparison().isEqualTo(expectedFilms);

    }

    @Test
    public void addFilmToWatchlistReturnsWatchlistResponseForUpdatedWatchlist() {
        Short watchlistId = watchlists.get(0).getId();
        Short filmId = films.get(0).getId();
        Watchlist mockWatchlist = watchlists.get(0);

        WatchlistResponse expectedWatchlist = WatchlistResponse.from(mockWatchlist);

        doReturn(mockWatchlist).when(wService).addFilmToWatchlist(watchlistId, filmId);

        WatchlistResponse actualWatchlist = controller.addFilmToWatchlist(watchlistId, filmId);

        assertNotNull(actualWatchlist);
        assertThat(actualWatchlist).usingRecursiveComparison().isEqualTo(expectedWatchlist);

    }
}
