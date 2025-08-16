package com.movieflix.service;

import com.movieflix.dto.request.MovieSearchRequest;
import com.movieflix.dto.response.MovieResponse;
import com.movieflix.dto.response.PagedMovieResponse;
import com.movieflix.model.Movie;
import com.movieflix.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    private final MovieRepository movieRepository;
    private final TMDBApiService tmdbApiService;
    private final CacheService cacheService;

    public MovieService(MovieRepository movieRepository, TMDBApiService tmdbApiService, CacheService cacheService) {
        this.movieRepository = movieRepository;
        this.tmdbApiService = tmdbApiService;
        this.cacheService = cacheService;
    }

    /**
     * Search movies with cache-first approach
     */
    public PagedMovieResponse searchMovies(MovieSearchRequest request) {
        logger.debug("Searching movies with query: {}", request.getQuery());

        // First try to search in local cache
        PagedMovieResponse cacheResults = searchInCache(request);

        // If we have sufficient results from cache, return them
        if (cacheResults.getMovies().size() >= request.getSize() ||
                !cacheResults.getMovies().isEmpty()) {
            logger.debug("Returning {} movies from cache", cacheResults.getMovies().size());
            return cacheResults;
        }

        // If cache doesn't have enough results, fetch from TMDB
        try {
            List<Movie> tmdbMovies = tmdbApiService.searchMovies(request.getQuery(), request.getPage());

            // Save fetched movies to cache
            List<Movie> savedMovies = tmdbMovies.stream()
                    .map(cacheService::saveToCache)
                    .collect(Collectors.toList());

            logger.debug("Fetched and cached {} movies from TMDB", savedMovies.size());

            // Apply filters and sorting to the fresh results
            List<MovieResponse> movieResponses = savedMovies.stream()
                    .filter(movie -> matchesFilters(movie, request))
                    .sorted(createComparator(request))
                    .skip((request.getPage() - 1) * request.getSize())
                    .limit(request.getSize())
                    .map(movie -> new MovieResponse(movie, false))
                    .collect(Collectors.toList());

            return new PagedMovieResponse(movieResponses, request.getPage(),
                    calculateTotalPages(savedMovies.size(), request.getSize()),
                    savedMovies.size(), request.getSize());

        } catch (Exception e) {
            logger.error("Error fetching movies from TMDB: {}", e.getMessage());
            // Fallback to cache results even if limited
            return cacheResults;
        }
    }

    /**
     * Get movie details by ID (cache-first)
     */
    public MovieResponse getMovieById(String id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            return new MovieResponse(movie.get(), true);
        }
        throw new RuntimeException("Movie not found with id: " + id);
    }

    /**
     * Get movie details by TMDB ID (cache-first)
     */
    public MovieResponse getMovieByTmdbId(Integer tmdbId) {
        // Check cache first
        Movie cachedMovie = cacheService.getMovieFromCache(tmdbId);
        if (cachedMovie != null) {
            logger.debug("Movie found in cache: {}", tmdbId);
            return new MovieResponse(cachedMovie, true);
        }

        // Fetch from TMDB if not in cache
        try {
            Movie tmdbMovie = tmdbApiService.getMovieDetails(tmdbId);
            if (tmdbMovie != null) {
                Movie savedMovie = cacheService.saveToCache(tmdbMovie);
                logger.debug("Movie fetched from TMDB and cached: {}", tmdbId);
                return new MovieResponse(savedMovie, false);
            }
        } catch (Exception e) {
            logger.error("Error fetching movie details from TMDB: {}", e.getMessage());
        }

        throw new RuntimeException("Movie not found with TMDB ID: " + tmdbId);
    }

    /**
     * Search in cache
     */
    private PagedMovieResponse searchInCache(MovieSearchRequest request) {
        Pageable pageable = createPageable(request);
        Page<Movie> moviePage = movieRepository.findByTitleContainingIgnoreCase(request.getQuery(), pageable);

        List<MovieResponse> movieResponses = moviePage.getContent().stream()
                .filter(movie -> matchesFilters(movie, request))
                .map(movie -> new MovieResponse(movie, true))
                .collect(Collectors.toList());

        return new PagedMovieResponse(movieResponses, request.getPage(),
                moviePage.getTotalPages(), moviePage.getTotalElements(), request.getSize());
    }

    /**
     * Create pageable object with sorting
     */
    private Pageable createPageable(MovieSearchRequest request) {
        Sort sort = createSort(request);
        return PageRequest.of(request.getPage() - 1, request.getSize(), sort);
    }

    /**
     * Create sort object
     */
    private Sort createSort(MovieSearchRequest request) {
        Sort.Direction direction = "desc".equalsIgnoreCase(request.getSortDirection())
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        return switch (request.getSortBy().toLowerCase()) {
            case "year" -> Sort.by(direction, "year");
            case "rating" -> Sort.by(direction, "rating.tmdb");
            case "runtime" -> Sort.by(direction, "runtime");
            default -> Sort.by(direction, "title");
        };
    }

    /**
     * Create comparator for in-memory sorting
     */
    private java.util.Comparator<Movie> createComparator(MovieSearchRequest request) {
        java.util.Comparator<Movie> comparator = switch (request.getSortBy().toLowerCase()) {
            case "year" -> java.util.Comparator.comparing(Movie::getYear,
                    java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder()));
            case "rating" -> java.util.Comparator.comparing(movie ->
                            movie.getRating() != null ? movie.getRating().getTmdb() : 0.0,
                    java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder()));
            case "runtime" -> java.util.Comparator.comparing(Movie::getRuntime,
                    java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder()));
            default -> java.util.Comparator.comparing(Movie::getTitle,
                    java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder()));
        };

        return "desc".equalsIgnoreCase(request.getSortDirection()) ? comparator.reversed() : comparator;
    }

    /**
     * Check if movie matches filters
     */
    private boolean matchesFilters(Movie movie, MovieSearchRequest request) {
        // Genre filter
        if (request.getGenre() != null && !request.getGenre().isEmpty()) {
            if (movie.getGenre() == null ||
                    movie.getGenre().stream().noneMatch(genre ->
                            genre.toLowerCase().contains(request.getGenre().toLowerCase()))) {
                return false;
            }
        }

        // Year filter
        if (request.getYear() != null) {
            if (movie.getYear() == null || !movie.getYear().equals(request.getYear())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Calculate total pages
     */
    private int calculateTotalPages(int totalElements, int pageSize) {
        return (int) Math.ceil((double) totalElements / pageSize);
    }

    /**
     * Get movies by genre
     */
    public List<MovieResponse> getMoviesByGenre(String genre) {
        List<Movie> movies = movieRepository.findByGenreContainingIgnoreCase(genre);
        return movies.stream()
                .map(movie -> new MovieResponse(movie, true))
                .collect(Collectors.toList());
    }

    /**
     * Get movies by year
     */
    public List<MovieResponse> getMoviesByYear(Integer year) {
        List<Movie> movies = movieRepository.findByYear(year);
        return movies.stream()
                .map(movie -> new MovieResponse(movie, true))
                .collect(Collectors.toList());
    }
}
