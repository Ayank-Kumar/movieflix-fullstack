package com.movieflix.external.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDBCastMember {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("character")
    private String character;

    @JsonProperty("order")
    private Integer order;

    // Constructors
    public TMDBCastMember() {}

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCharacter() { return character; }
    public void setCharacter(String character) { this.character = character; }

    public Integer getOrder() { return order; }
    public void setOrder(Integer order) { this.order = order; }
}
