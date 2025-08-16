package com.movieflix.dto.response;

import java.util.List;

public class PagedMovieResponse {

    private List<MovieResponse> movies;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size;
    private boolean hasNext;
    private boolean hasPrevious;

    // Constructors
    public PagedMovieResponse() {}

    public PagedMovieResponse(List<MovieResponse> movies, int currentPage, int totalPages,
                              long totalElements, int size) {
        this.movies = movies;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.hasNext = currentPage < totalPages;
        this.hasPrevious = currentPage > 1;
    }

    // Getters and Setters
    public List<MovieResponse> getMovies() { return movies; }
    public void setMovies(List<MovieResponse> movies) { this.movies = movies; }

    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public boolean isHasNext() { return hasNext; }
    public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }

    public boolean isHasPrevious() { return hasPrevious; }
    public void setHasPrevious(boolean hasPrevious) { this.hasPrevious = hasPrevious; }
}
