package com.pilot.sakila.dto.request;


import com.pilot.sakila.dto.ValidationGroup;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static com.pilot.sakila.dto.ValidationGroup.*;

@AllArgsConstructor
@Getter


public class ActorRequest {
    @NotNull(groups = {Create.class})
    @Size(min = 1, max = 45)
    private final String firstName;

    @NotNull(groups = {Create.class})
    @Size(min = 1, max = 45)
    private final String lastName;

    @NotNull(groups = {Create.class})
    private final List<Short> filmIds;
}
