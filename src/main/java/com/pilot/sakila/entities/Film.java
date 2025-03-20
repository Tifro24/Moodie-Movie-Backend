package com.pilot.sakila.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pilot.sakila.dto.response.PartialActorResponse;
import com.pilot.sakila.enums.Rating;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Table(name = "film")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

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

    @Column(name = "length")
    private Short length;


    @Column(name = "rating")
    private Rating rating;

    @ManyToMany
    @JoinTable(
            name = "film_actor",
            joinColumns = {@JoinColumn(name = "film_id")},
            inverseJoinColumns = {@JoinColumn(name = "actor_id")}
    )
    @Getter(AccessLevel.NONE)
    private List<Actor> cast;

    @JsonIgnore
    @ManyToMany(mappedBy = "films")
    private List<Category> categories;

    public List<PartialActorResponse> getCast(){
        return cast.stream()
                .map(PartialActorResponse::from)
                .collect(Collectors.toList());
    }


    @ManyToMany(mappedBy = "films")
    private List<Watchlist> watchlists;




}
