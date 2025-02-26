package com.pilot.sakila.repository;

import com.pilot.sakila.entities.FilmText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FilmTextRepository extends JpaRepository<FilmText, Short> {

    // Custom method to delete film_text entries by film_id
    @Modifying
    @Transactional
    @Query("DELETE FROM FilmText ft WHERE ft.film.id = :filmId")
    void deleteByFilmId(Short filmId);
}