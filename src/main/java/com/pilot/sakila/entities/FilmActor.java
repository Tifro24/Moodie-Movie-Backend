package com.pilot.sakila.entities;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "film_actor")
@Getter

public class FilmActor {

    @EmbeddedId
    private FilmActorId id;

    @ManyToOne
    @JoinColumn(name = "actor_id", referencedColumnName = "actor_id", insertable = false, updatable = false)
    private Actor actor;

    @ManyToOne
    @JoinColumn(name = "film_id", referencedColumnName = "film_id", insertable = false, updatable = false)
    private Film film;
}
