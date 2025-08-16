package com.movieflix.external.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDBMovieResponse {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("imdb_id")
    private String imdbId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("original_title")
    private String originalTitle;

    @JsonProperty("release_date")
    private String releaseDate; // "2023-07-21" format

    @JsonProperty("runtime")
    private Integer runtime;

    @JsonProperty("genres")
    private List<TMDBGenre> genres;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("vote_average")
    private Double voteAverage;

    @JsonProperty("vote_count")
    private Integer voteCount;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("budget")
    private Long budget;

    @JsonProperty("revenue")
    private Long revenue;

    @JsonProperty("status")
    private String status;

    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("production_companies")
    private List<TMDBProductionCompany> productionCompanies;

    @JsonProperty("credits")
    private TMDBCredits credits;

    // Constructors
    public TMDBMovieResponse() {}

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getImdbId() { return imdbId; }
    public void setImdbId(String imdbId) { this.imdbId = imdbId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getOriginalTitle() { return originalTitle; }
    public void setOriginalTitle(String originalTitle) { this.originalTitle = originalTitle; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public Integer getRuntime() { return runtime; }
    public void setRuntime(Integer runtime) { this.runtime = runtime; }

    public List<TMDBGenre> getGenres() { return genres; }
    public void setGenres(List<TMDBGenre> genres) { this.genres = genres; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public Double getVoteAverage() { return voteAverage; }
    public void setVoteAverage(Double voteAverage) { this.voteAverage = voteAverage; }

    public Integer getVoteCount() { return voteCount; }
    public void setVoteCount(Integer voteCount) { this.voteCount = voteCount; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getBackdropPath() { return backdropPath; }
    public void setBackdropPath(String backdropPath) { this.backdropPath = backdropPath; }

    public Long getBudget() { return budget; }
    public void setBudget(Long budget) { this.budget = budget; }

    public Long getRevenue() { return revenue; }
    public void setRevenue(Long revenue) { this.revenue = revenue; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOriginalLanguage() { return originalLanguage; }
    public void setOriginalLanguage(String originalLanguage) { this.originalLanguage = originalLanguage; }

    public List<TMDBProductionCompany> getProductionCompanies() { return productionCompanies; }
    public void setProductionCompanies(List<TMDBProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public TMDBCredits getCredits() { return credits; }
    public void setCredits(TMDBCredits credits) { this.credits = credits; }
}
