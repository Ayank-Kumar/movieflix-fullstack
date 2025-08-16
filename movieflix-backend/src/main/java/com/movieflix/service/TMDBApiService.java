package com.movieflix.service;

import com.movieflix.external.tmdb.*;
import com.movieflix.external.tmdb.TMDBMovieResponse;
import com.movieflix.model.Movie;
import com.movieflix.model.Rating;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TMDBApiService {

    private static final Logger logger = LoggerFactory.getLogger(TMDBApiService.class);

    private final RestTemplate restTemplate;
    private Map<Integer, String> genreMap = new HashMap<>();

    @Value("${movieflix.tmdb.api-key}")
    private String apiKey;

    @Value("${movieflix.tmdb.base-url}")
    private String baseUrl;

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public TMDBApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void initializeGenreMap() {
        try {
            String url = String.format("%s/genre/movie/list?api_key=%s", baseUrl, apiKey);
            logger.info("Fetching TMDB genre list from: {}", url.replace(apiKey, "***"));

            TMDBGenreListResponse response = restTemplate.getForObject(url, TMDBGenreListResponse.class);

            if (response != null && response.getGenres() != null) {
                genreMap = response.getGenres().stream()
                        .collect(Collectors.toMap(TMDBGenre::getId, TMDBGenre::getName));
                logger.info("Loaded {} genres from TMDB", genreMap.size());
            } else {
                logger.warn("Failed to load genres from TMDB");
                // Fallback to basic genre mapping
                initializeFallbackGenres();
            }
        } catch (Exception e) {
            logger.error("Error fetching TMDB genre list: {}", e.getMessage());
            // Fallback to basic genre mapping
            initializeFallbackGenres();
        }
    }

    private void initializeFallbackGenres() {
        // Popular TMDB genre IDs and names as fallback
        genreMap.put(28, "Action");
        genreMap.put(12, "Adventure");
        genreMap.put(16, "Animation");
        genreMap.put(35, "Comedy");
        genreMap.put(80, "Crime");
        genreMap.put(99, "Documentary");
        genreMap.put(18, "Drama");
        genreMap.put(10751, "Family");
        genreMap.put(14, "Fantasy");
        genreMap.put(36, "History");
        genreMap.put(27, "Horror");
        genreMap.put(10402, "Music");
        genreMap.put(9648, "Mystery");
        genreMap.put(10749, "Romance");
        genreMap.put(878, "Science Fiction");
        genreMap.put(10770, "TV Movie");
        genreMap.put(53, "Thriller");
        genreMap.put(10752, "War");
        genreMap.put(37, "Western");
        logger.info("Loaded {} fallback genres", genreMap.size());
    }

    /**
     * Search movies by title
     */
    public List<Movie> searchMovies(String query, int page) {
        try {
            String url = String.format("%s/search/movie?api_key=%s&query=%s&page=%d",
                    baseUrl, apiKey, query, page);

            logger.debug("Searching movies with URL: {}", url.replace(apiKey, "***"));

            TMDBSearchResponse response = restTemplate.getForObject(url, TMDBSearchResponse.class);

            if (response == null || response.getResults() == null) {
                logger.warn("Empty response from TMDB search API");
                return new ArrayList<>();
            }

            return response.getResults().stream()
                    .map(this::convertSearchResultToMovie)
                    .collect(Collectors.toList());

        } catch (RestClientException e) {
            logger.error("Error calling TMDB search API: {}", e.getMessage());
            throw new RuntimeException("Failed to search movies from TMDB", e);
        }
    }

    /**
     * Get movie details by TMDB ID
     */
    public Movie getMovieDetails(Integer tmdbId) {
        try {
            String url = String.format("%s/movie/%d?api_key=%s&append_to_response=credits",
                    baseUrl, tmdbId, apiKey);

            logger.debug("Getting movie details with URL: {}", url.replace(apiKey, "***"));

            TMDBMovieResponse response = restTemplate.getForObject(url, TMDBMovieResponse.class);

            if (response == null) {
                logger.warn("Empty response from TMDB movie details API for ID: {}", tmdbId);
                return null;
            }

            return convertDetailResponseToMovie(response);

        } catch (RestClientException e) {
            logger.error("Error calling TMDB movie details API: {}", e.getMessage());
            throw new RuntimeException("Failed to get movie details from TMDB", e);
        }
    }

    /**
     * Convert TMDB search result to Movie entity
     */
    private Movie convertSearchResultToMovie(TMDBMovieSearchResult searchResult) {
        Movie movie = new Movie();
        movie.setTmdbId(searchResult.getId());
        movie.setTitle(searchResult.getTitle());
        movie.setPlot(searchResult.getOverview());

        // Extract year from release date
        if (searchResult.getReleaseDate() != null && !searchResult.getReleaseDate().isEmpty()) {
            try {
                Date releaseDate = dateFormat.parse(searchResult.getReleaseDate());
                movie.setReleaseDate(releaseDate);
                movie.setYear(Integer.parseInt(searchResult.getReleaseDate().substring(0, 4)));
            } catch (ParseException e) {
                logger.warn("Failed to parse release date: {}", searchResult.getReleaseDate());
            }
        }

        // Map genre IDs to genre names
        if (searchResult.getGenreIds() != null && !searchResult.getGenreIds().isEmpty()) {
            List<String> genres = searchResult.getGenreIds().stream()
                    .map(genreMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            movie.setGenre(genres);
            logger.debug("Mapped genres for movie {}: {}", movie.getTitle(), genres);
        }

        // Set rating
        Rating rating = new Rating();
        rating.setTmdb(searchResult.getVoteAverage());
        rating.setTmdbVoteCount(searchResult.getVoteCount());
        movie.setRating(rating);

        // Set image URLs
        if (searchResult.getPosterPath() != null) {
            movie.setPoster(IMAGE_BASE_URL + searchResult.getPosterPath());
        }
        if (searchResult.getBackdropPath() != null) {
            movie.setBackdrop(IMAGE_BASE_URL + searchResult.getBackdropPath());
        }

        movie.setLanguage(searchResult.getOriginalLanguage());

        return movie;
    }

    /**
     * Convert TMDB detailed response to Movie entity
     */
    private Movie convertDetailResponseToMovie(TMDBMovieResponse response) {
        Movie movie = new Movie();
        movie.setTmdbId(response.getId());
        movie.setImdbId(response.getImdbId());
        movie.setTitle(response.getTitle());
        movie.setPlot(response.getOverview());
        movie.setRuntime(response.getRuntime());
        movie.setBudget(response.getBudget());
        movie.setRevenue(response.getRevenue());
        movie.setStatus(response.getStatus());
        movie.setLanguage(response.getOriginalLanguage());

        // Extract year and set release date
        if (response.getReleaseDate() != null && !response.getReleaseDate().isEmpty()) {
            try {
                Date releaseDate = dateFormat.parse(response.getReleaseDate());
                movie.setReleaseDate(releaseDate);
                movie.setYear(Integer.parseInt(response.getReleaseDate().substring(0, 4)));
            } catch (ParseException e) {
                logger.warn("Failed to parse release date: {}", response.getReleaseDate());
            }
        }

        // Set genres from detailed response
        if (response.getGenres() != null) {
            List<String> genres = response.getGenres().stream()
                    .map(TMDBGenre::getName)
                    .collect(Collectors.toList());
            movie.setGenre(genres);
        }

        // Set rating
        Rating rating = new Rating();
        rating.setTmdb(response.getVoteAverage());
        rating.setTmdbVoteCount(response.getVoteCount());
        movie.setRating(rating);

        // Set image URLs
        if (response.getPosterPath() != null) {
            movie.setPoster(IMAGE_BASE_URL + response.getPosterPath());
        }
        if (response.getBackdropPath() != null) {
            movie.setBackdrop(IMAGE_BASE_URL + response.getBackdropPath());
        }

        // Extract cast and crew
        if (response.getCredits() != null) {
            // Extract top 10 actors
            if (response.getCredits().getCast() != null) {
                List<String> actors = response.getCredits().getCast().stream()
                        .limit(10)
                        .map(TMDBCastMember::getName)
                        .collect(Collectors.toList());
                movie.setActors(actors);
            }

            // Extract directors
            if (response.getCredits().getCrew() != null) {
                List<String> directors = response.getCredits().getCrew().stream()
                        .filter(crew -> "Director".equals(crew.getJob()))
                        .map(TMDBCrewMember::getName)
                        .collect(Collectors.toList());
                movie.setDirector(directors);
            }
        }

        return movie;
    }
}
