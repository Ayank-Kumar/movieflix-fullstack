package com.movieflix.dto.response;

import com.movieflix.model.Movie;
import com.movieflix.model.Rating;

import java.util.Date;
import java.util.List;

public class MovieResponse {

    private String id;
    private Integer tmdbId;
    private String imdbId;
    private String title;
    private Integer year;
    private Integer runtime;
    private List<String> genre;
    private List<String> director;
    private List<String> actors;
    private String plot;
    private Rating rating;
    private String poster;
    private String backdrop;
    private Date releaseDate;
    private Long budget;
    private Long revenue;
    private String language;
    private String status;
    private boolean fromCache;

    // Constructors
    public MovieResponse() {}

    public MovieResponse(Movie movie, boolean fromCache) {
        this.id = movie.getId();
        this.tmdbId = movie.getTmdbId();
        this.imdbId = movie.getImdbId();
        this.title = movie.getTitle();
        this.year = movie.getYear();
        this.runtime = movie.getRuntime();
        this.genre = movie.getGenre();
        this.director = movie.getDirector();
        this.actors = movie.getActors();
        this.plot = movie.getPlot();
        this.rating = movie.getRating();
        this.poster = movie.getPoster();
        this.backdrop = movie.getBackdrop();
        this.releaseDate = movie.getReleaseDate();
        this.budget = movie.getBudget();
        this.revenue = movie.getRevenue();
        this.language = movie.getLanguage();
        this.status = movie.getStatus();
        this.fromCache = fromCache;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Integer getTmdbId() { return tmdbId; }
    public void setTmdbId(Integer tmdbId) { this.tmdbId = tmdbId; }

    public String getImdbId() { return imdbId; }
    public void setImdbId(String imdbId) { this.imdbId = imdbId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Integer getRuntime() { return runtime; }
    public void setRuntime(Integer runtime) { this.runtime = runtime; }

    public List<String> getGenre() { return genre; }
    public void setGenre(List<String> genre) { this.genre = genre; }

    public List<String> getDirector() { return director; }
    public void setDirector(List<String> director) { this.director = director; }

    public List<String> getActors() { return actors; }
    public void setActors(List<String> actors) { this.actors = actors; }

    public String getPlot() { return plot; }
    public void setPlot(String plot) { this.plot = plot; }

    public Rating getRating() { return rating; }
    public void setRating(Rating rating) { this.rating = rating; }

    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }

    public String getBackdrop() { return backdrop; }
    public void setBackdrop(String backdrop) { this.backdrop = backdrop; }

    public Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }

    public Long getBudget() { return budget; }
    public void setBudget(Long budget) { this.budget = budget; }

    public Long getRevenue() { return revenue; }
    public void setRevenue(Long revenue) { this.revenue = revenue; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isFromCache() { return fromCache; }
    public void setFromCache(boolean fromCache) { this.fromCache = fromCache; }
}
