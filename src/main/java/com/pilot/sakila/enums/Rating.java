package com.pilot.sakila.enums;

public enum Rating {
    G("General Audience"),
    PG("Parental Guidance Suggested"),
    PG_13("Parents Strongly Cautioned"),
    R("Restricted"),
    NC_17("Adults Only");

    private final String description;

    Rating(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    @Override
    public String toString(){
        switch (this){
            case PG_13 : return "PG-13";
            case NC_17: return "NC-17";
            default: return super.toString();
        }
    }

    public static Rating fromString(String rating){
        switch(rating){
            case "PG-13" : return PG_13;
            case "NC-17" : return NC_17;
            default:return Rating.valueOf(rating);
        }
    }
}