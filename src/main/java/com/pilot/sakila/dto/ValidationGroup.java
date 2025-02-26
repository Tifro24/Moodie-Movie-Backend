package com.pilot.sakila.dto;

import jakarta.validation.groups.Default;
import lombok.Builder;

public class ValidationGroup {
    public interface Create extends Default {}
    public interface Update extends Default {}
}
