package com.movieflix.external.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDBCredits {

    @JsonProperty("cast")
    private List<TMDBCastMember> cast;

    @JsonProperty("crew")
    private List<TMDBCrewMember> crew;

    // Constructors
    public TMDBCredits() {}

    // Getters and Setters
    public List<TMDBCastMember> getCast() { return cast; }
    public void setCast(List<TMDBCastMember> cast) { this.cast = cast; }

    public List<TMDBCrewMember> getCrew() { return crew; }
    public void setCrew(List<TMDBCrewMember> crew) { this.crew = crew; }
}
