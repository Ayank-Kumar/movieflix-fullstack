package com.movieflix.external.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDBSearchResponse {

    @JsonProperty("page")
    private Integer page;

    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("total_results")
    private Integer totalResults;

    @JsonProperty("results")
    private List<TMDBMovieSearchResult> results;

    // Constructors
    public TMDBSearchResponse() {}

    // Getters and Setters
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getTotalPages() { return totalPages; }
    public void setTotalPages(Integer totalPages) { this.totalPages = totalPages; }

    public Integer getTotalResults() { return totalResults; }
    public void setTotalResults(Integer totalResults) { this.totalResults = totalResults; }

    public List<TMDBMovieSearchResult> getResults() { return results; }
    public void setResults(List<TMDBMovieSearchResult> results) { this.results = results; }
}
