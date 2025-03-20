package com.pilot.sakila.controller;

import com.pilot.sakila.dto.response.CategoryResponseForPref;
import com.pilot.sakila.entities.Preferences;
import com.pilot.sakila.services.PreferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/preferences")
public class PreferencesController {
    @Autowired
    private PreferencesService preferencesService;

    @GetMapping("/byMood")
    public List<CategoryResponseForPref.CategoryResponse> getPreferencesByMood(@RequestParam String mood) {
        return preferencesService.getPreferencesByMood(mood);
    }

    @GetMapping
    public List<Preferences> getAllMoods(){
        return preferencesService.getAllMoods();
    }


    @PostMapping Preferences createPreference(@RequestBody Preferences preference) {
        return preferencesService.savePreference(preference);
    }
}
