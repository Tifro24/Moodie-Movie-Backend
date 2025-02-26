package com.pilot.sakila.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter


public class ActorRequest {
    private final String firstName;
    private final String lastName;
    private final List<Short> filmIds;
}
