package com.pilot.sakila.entities;

import com.pilot.sakila.dto.response.PartialActorResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "film")
@Getter


public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Short id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "release_year")
    private Year releaseYear;
    @ManyToOne
    @JoinColumn(name = "language_id", referencedColumnName = "language_id")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "original_language_id", referencedColumnName = "language_id")
    private Language originalLanguage;

    @Column(name = "rental_duration")
    private Byte rentalDuration;

    @Column(name = "rental_rate")
    private BigDecimal rentalRate;

    @Column(name = "length")
    private Short length;

    @ManyToMany(mappedBy = "films")
    @Getter(AccessLevel.NONE)
    private List<Actor> cast;

    @ManyToMany(mappedBy = "films")
    @Getter(AccessLevel.NONE)
    private List<Category> categories;

    public List<PartialActorResponse> getCast(){
        return cast.stream()
                .map(PartialActorResponse::from)
                .collect(Collectors.toList());
    }


}
