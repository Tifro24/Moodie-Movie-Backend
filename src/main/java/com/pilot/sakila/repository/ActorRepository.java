package com.pilot.sakila.repository;

import com.pilot.sakila.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Short> {
    List<Actor> findByFullNameContainingIgnoreCase(String firstName);

}
