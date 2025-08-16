package com.movieflix.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.data.mongodb.core.index.Ttl;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Document(collection = "movies")
public class Movie {

    @Id
    private String id; // MongoDB ObjectId

    @NotNull
    @Indexed(unique = true)
    private Integer tmdbId; // TMDB movie ID for uniqueness

    private String imdbId; // IMDb ID (optional)

    @NotBlank
    @Indexed
    private String title;

    @Indexed
    private Integer year;

    private Integer runtime; // in minutes

    @Indexed
    private List<String> genre;

    private List<String> director;

    private List<String> actors;

    private String plot;

    private Rating rating;

    private String poster; // poster image URL

    private String backdrop; // backdrop image URL

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date releaseDate;

    private Long budget;

    private Long revenue;

    private String language;

    private String status; // Released, In Production, etc.

    // Cache management fields
    @Indexed
    private Date createdAt;

    private Date updatedAt;

    @SuppressWarnings("removal")
    @Indexed(expireAfterSeconds = 86400)  // 24 hours TTL
    private Date cacheExpiry;

    // Constructors
    public Movie() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
        this.cacheExpiry = new Date(now.getTime() + 24 * 60 * 60 * 1000);
    }

    public Movie(Integer tmdbId, String title) {
        this();
        this.tmdbId = tmdbId;
        this.title = title;
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

    public Long getBudget() { return budget; }
    public void setBudget(Long budget) { this.budget = budget; }

    public Long getRevenue() { return revenue; }
    public void setRevenue(Long revenue) { this.revenue = revenue; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCacheExpiry() {
        return cacheExpiry;
    }

    public void setCacheExpiry(Date cacheExpiry) {
        this.cacheExpiry = cacheExpiry;
    }

    // Helper method to update cache expiry
    public void refreshCache() {
        Date now = new Date();
        this.updatedAt = now;
        this.cacheExpiry = new Date(now.getTime() + 24 * 60 * 60 * 1000);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", tmdbId=" + tmdbId +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", genre=" + genre +
                '}';
    }
}
