package com.movieflix.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class MovieSearchRequest {

    @NotBlank(message = "Search query cannot be empty")
    private String query;

    @Min(value = 1, message = "Page must be at least 1")
    private int page = 1;

    @Min(value = 1, message = "Size must be at least 1")
    @Max(value = 50, message = "Size cannot exceed 50")
    private int size = 20;

    private String genre;
    private Integer year;
    private String sortBy = "title"; // title, year, rating
    private String sortDirection = "asc"; // asc, desc

    // Constructors
    public MovieSearchRequest() {}

    public MovieSearchRequest(String query) {
        this.query = query;
    }

    // Getters and Setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }
}
