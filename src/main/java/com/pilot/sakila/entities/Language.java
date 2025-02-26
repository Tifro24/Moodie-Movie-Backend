package com.pilot.sakila.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "language")
@Getter


public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Byte id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "language")
    @Getter(AccessLevel.NONE)
    private List<Film> films;

}
