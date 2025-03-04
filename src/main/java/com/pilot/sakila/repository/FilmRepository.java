package com.pilot.sakila.repository;

import com.pilot.sakila.entities.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Short> {
    List<Film> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT f FROM Film f JOIN f.categories c WHERE c.name IN :categories")
    List<Film> findFilmsByGenres(@Param("categories") List<String> categories);
}
