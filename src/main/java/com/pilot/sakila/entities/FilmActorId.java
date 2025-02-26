package com.pilot.sakila.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import java.io.Serializable;
@Getter
@Embeddable
public class FilmActorId implements Serializable {

    @Column(name = "actor_id")
    private Long actorId;

    @Column(name = "film_id")
    private Long filmId;
}
