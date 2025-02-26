package com.pilot.sakila.repository;

import com.pilot.sakila.entities.FilmActor;
import com.pilot.sakila.entities.FilmActorId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface FilmActorRepository extends JpaRepository<FilmActor, FilmActorId>{
    @Modifying
    @Transactional
    @Query("DELETE FROM FilmActor fa WHERE fa.actor.id = :actorId")
    void deleteByActorId(Short actorId);

    @Modifying
    @Transactional
    @Query("DELETE FROM FilmActor fa WHERE fa.film.id = :filmId")
    void deleteByFilmId(Short filmId);

}
