package com.pilot.sakila.repository;

import com.pilot.sakila.entities.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Short> {
    List<Film> findByTitleContainingIgnoreCase(String title);

}
