package com.pilot.sakila.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name="preferences")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class Preferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Short id;

    @Column(name = "mood")
    private String mood;

    @JsonIgnore
    @ManyToMany(mappedBy = "preferences")
    private List<Category> genres;

    @Column(name = "created_at")
    private Timestamp createdAt;

}
