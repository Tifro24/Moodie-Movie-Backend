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
                    // For each preference, get the genres (categories) and films related to it
                    return preference.getGenres().stream() // Iterate over genres (categories)
                            .map(category -> new CategoryResponseForPref.CategoryResponse(
                                    preference.getId(),  // Preference ID
                                    preference.getMood(), // Mood (e.g., happy)
                                    category.getName(),   // Category Name (e.g., Action)
                                    category.getFilms().stream()  // Films related to the category
                                            .map(film -> new CategoryResponseForPref.FilmResponse(film.getTitle(), film.getDescription()))
                                            .collect(Collectors.toList())  // Collect films in a list
                            ))
                            .collect(Collectors.toList());
                })
                .flatMap(Collection::stream) // Flatten the list of lists into a single list
                .collect(Collectors.toList());
    }

    public Preferences savePreference(Preferences preference){
        return preferencesRepository.save(preference);
    }
}
