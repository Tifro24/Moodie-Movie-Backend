package com.pilot.sakila.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "language")
@Getter
@AllArgsConstructor
@NoArgsConstructor

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
