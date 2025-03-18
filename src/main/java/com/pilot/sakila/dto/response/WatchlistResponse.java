package com.pilot.sakila.dto.response;

import com.pilot.sakila.entities.Watchlist;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WatchlistResponse {

    private final Short id;
    private final String name;
    private final MoodResponse mood;
    private final List<MiniFilmResponse> films;


    public static WatchlistResponse from(Watchlist watchlist) {
        return new WatchlistResponse(
                watchlist.getId(),
                watchlist.getName(),
                MoodResponse.from(watchlist.getMood()),
                watchlist.getFilms().stream().map(MiniFilmResponse::from).toList()
        );
    }
}
