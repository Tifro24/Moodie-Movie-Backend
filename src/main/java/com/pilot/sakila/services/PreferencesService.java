package com.pilot.sakila.services;

import com.pilot.sakila.dto.response.CategoryResponseForPref;
import com.pilot.sakila.entities.Preferences;
import com.pilot.sakila.repository.PreferencesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PreferencesService {

    @Autowired
    private PreferencesRepository preferencesRepository;

    public List<CategoryResponseForPref.CategoryResponse> getPreferencesByMood(String mood){
        List<Preferences> preferences = preferencesRepository.findByMood(mood);

        // Map the Preferences to CategoryResponseForPref to exclude category IDs and return films
        return preferences.stream()
                .map(preference -> {
                    return preference.getGenres().stream()
                            .map(category -> new CategoryResponseForPref.CategoryResponse(
                                    preference.getId(),
                                    preference.getMood(),
                                    category.getName(),
                                    category.getFilms().stream()
                                            .map(film -> new CategoryResponseForPref.FilmResponse(film.getTitle(), film.getDescription()))
                                            .collect(Collectors.toList())
                            ))
                            .collect(Collectors.toList());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public Preferences savePreference(Preferences preference){
        return preferencesRepository.save(preference);
    }
}
