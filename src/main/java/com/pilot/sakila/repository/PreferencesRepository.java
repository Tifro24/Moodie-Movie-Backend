package com.pilot.sakila.repository;

import com.pilot.sakila.entities.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferencesRepository extends JpaRepository<Preferences, Short> {
    List<Preferences> findByMood(String mood);
}
