package com.pilot.sakila.repository;

import com.pilot.sakila.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Short> {
}
