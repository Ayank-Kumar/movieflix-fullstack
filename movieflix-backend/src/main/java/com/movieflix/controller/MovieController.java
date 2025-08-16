package com.movieflix.controller;

import com.movieflix.dto.request.MovieSearchRequest;
import com.movieflix.dto.response.MovieResponse;
import com.movieflix.dto.response.PagedMovieResponse;
import com.movieflix.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "http://localhost:3000") // For frontend
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Search movies with filters and pagination
     * GET /api/movies?search=batman&page=1&size=20&genre=Action&year=2023&sortBy=rating&sortDirection=desc
     */
    @GetMapping
    public ResponseEntity<PagedMovieResponse> searchMovies(
            @RequestParam String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        MovieSearchRequest request = new MovieSearchRequest();
        request.setQuery(search);
        request.setPage(page);
        request.setSize(size);
        request.setGenre(genre);
        request.setYear(year);
        request.setSortBy(sortBy);
        request.setSortDirection(sortDirection);

        PagedMovieResponse response = movieService.searchMovies(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get movie by internal ID
     * GET /api/movies/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable String id) {
        MovieResponse movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie);
    }

    /**
     * Get movie by TMDB ID
     * GET /api/movies/tmdb/{tmdbId}
     */
    @GetMapping("/tmdb/{tmdbId}")
    public ResponseEntity<MovieResponse> getMovieByTmdbId(@PathVariable Integer tmdbId) {
        MovieResponse movie = movieService.getMovieByTmdbId(tmdbId);
        return ResponseEntity.ok(movie);
    }

    /**
     * Get movies by genre
     * GET /api/movies/genre/Action
     */
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieResponse>> getMoviesByGenre(@PathVariable String genre) {
        List<MovieResponse> movies = movieService.getMoviesByGenre(genre);
        return ResponseEntity.ok(movies);
    }

    /**
     * Get movies by year
     * GET /api/movies/year/2023
     */
    @GetMapping("/year/{year}")
    public ResponseEntity<List<MovieResponse>> getMoviesByYear(@PathVariable Integer year) {
        List<MovieResponse> movies = movieService.getMoviesByYear(year);
        return ResponseEntity.ok(movies);
    }

    /**
     * Get popular movies (mock endpoint for trending)
     * GET /api/movies/popular?limit=20
     */
    @GetMapping("/popular")
    public ResponseEntity<List<MovieResponse>> getPopularMovies(
            @RequestParam(defaultValue = "20") int limit) {
        // For now, return high-rated movies as "popular"
        // In a real app, you might track view counts or have different logic
        List<MovieResponse> popularMovies = movieService.getMoviesByGenre("Action")
                .stream()
                .limit(limit)
                .toList();
        return ResponseEntity.ok(popularMovies);
    }

    /**
     * Get available genres
     * GET /api/movies/genres
     */
    @GetMapping("/genres")
    public ResponseEntity<List<String>> getAvailableGenres() {
        // This would typically be cached or computed from your data
        List<String> genres = List.of(
                "Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary",
                "Drama", "Family", "Fantasy", "History", "Horror", "Music", "Mystery",
                "Romance", "Science Fiction", "TV Movie", "Thriller", "War", "Western"
        );
        return ResponseEntity.ok(genres);
    }

    /**
     * Get available years range
     * GET /api/movies/years
     */
    @GetMapping("/years")
    public ResponseEntity<Map<String, Integer>> getAvailableYears() {
        // In a real implementation, you'd query your database for min/max years
        Map<String, Integer> yearRange = Map.of(
                "minYear", 1900,
                "maxYear", 2024
        );
        return ResponseEntity.ok(yearRange);
    }

    @PostMapping("/search")
    public ResponseEntity<PagedMovieResponse> advancedSearch(@Valid @RequestBody MovieSearchRequest request) {
        PagedMovieResponse response = movieService.searchMovies(request);
        return ResponseEntity.ok(response);
    }
}
