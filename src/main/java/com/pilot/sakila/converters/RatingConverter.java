package com.pilot.sakila.converters;

import com.pilot.sakila.enums.Rating;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, String> {

    @Override
    public String convertToDatabaseColumn(Rating rating){
        if(rating == null){
            return null;
        }
        return rating.toString();
    }

    @Override
    public Rating convertToEntityAttribute(String DatabaseValue){
        if(DatabaseValue == null){
            return null;
        }
        return Rating.fromString(DatabaseValue);
    }
}


