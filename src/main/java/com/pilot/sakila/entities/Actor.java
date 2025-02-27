package com.pilot.sakila.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="actor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="actor_id")
    @Setter(AccessLevel.NONE)
    private short id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Formula("concat(first_name,' ', last_name)")
    @Setter(AccessLevel.NONE)
    private String fullName;

    @ManyToMany(mappedBy = "cast")

    private List<Film> films = new ArrayList<>();

    public String getFullName(){
        return firstName + " " + lastName;
    }
}

