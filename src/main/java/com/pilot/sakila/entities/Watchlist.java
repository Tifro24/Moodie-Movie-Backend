package com.pilot.sakila.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "watchlist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "watchlist_id")
    private Short id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "mood_id", referencedColumnName = "id" , nullable = false)
    private Preferences mood;

    @ManyToMany
    @JoinTable(
            name = "watchlist_film",
            joinColumns = @JoinColumn(name = "watchlist_id"),
            inverseJoinColumns = @JoinColumn(name = "film_id")
    )
    private List<Film> films = new ArrayList<>();

    @Column(name = "session_id", nullable = false)
    private String sessionId;



}
