package com.pilot.sakila.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "film_text")
@Getter
@Setter
@NoArgsConstructor
public class FilmText {

    @Id
    private Short id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "film_id")
    private Film film;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;
}
