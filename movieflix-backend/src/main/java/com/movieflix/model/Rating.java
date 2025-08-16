package com.movieflix.model;

public class Rating {
    private Double imdb;
    private Double tmdb;
    private Integer tmdbVoteCount;
    private String imdbRating; // Sometimes comes as string from APIs

    // Constructors
    public Rating() {}

    public Rating(Double imdb, Double tmdb) {
        this.imdb = imdb;
        this.tmdb = tmdb;
    }

    // Getters and Setters
    public Double getImdb() { return imdb; }
    public void setImdb(Double imdb) { this.imdb = imdb; }

    public Double getTmdb() { return tmdb; }
    public void setTmdb(Double tmdb) { this.tmdb = tmdb; }

    public Integer getTmdbVoteCount() { return tmdbVoteCount; }
    public void setTmdbVoteCount(Integer tmdbVoteCount) { this.tmdbVoteCount = tmdbVoteCount; }

    public String getImdbRating() { return imdbRating; }
    public void setImdbRating(String imdbRating) { this.imdbRating = imdbRating; }

    @Override
    public String toString() {
        return "Rating{" +
                "imdb=" + imdb +
                ", tmdb=" + tmdb +
                ", tmdbVoteCount=" + tmdbVoteCount +
                '}';
    }
}
