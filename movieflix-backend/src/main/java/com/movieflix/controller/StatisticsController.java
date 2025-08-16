package com.movieflix.controller;

import com.movieflix.model.Movie;
import com.movieflix.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "http://localhost:3000")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Get genre distribution for pie chart
     * GET /api/statistics/genre-distribution
     */
    @GetMapping("/genre-distribution")
    public ResponseEntity<Map<String, Object>> getGenreDistribution() {
        Map<String, Object> data = statisticsService.getGenreDistribution();
        return ResponseEntity.ok(data);
    }

    /**
     * Get average ratings by genre for bar chart
     * GET /api/statistics/ratings-by-genre
     */
    @GetMapping("/ratings-by-genre")
    public ResponseEntity<Map<String, Object>> getAverageRatingsByGenre() {
        Map<String, Object> data = statisticsService.getAverageRatingsByGenre();
        return ResponseEntity.ok(data);
    }

    /**
     * Get average runtime by year for line chart
     * GET /api/statistics/runtime-by-year
     */
    @GetMapping("/runtime-by-year")
    public ResponseEntity<Map<String, Object>> getAverageRuntimeByYear() {
        Map<String, Object> data = statisticsService.getAverageRuntimeByYear();
        return ResponseEntity.ok(data);
    }

    /**
     * Get top rated movies
     * GET /api/statistics/top-rated?limit=10
     */
    @GetMapping("/top-rated")
    public ResponseEntity<List<Movie>> getTopRatedMovies(
            @RequestParam(defaultValue = "10") int limit) {
        List<Movie> movies = statisticsService.getTopRatedMovies(limit);
        return ResponseEntity.ok(movies);
    }

    /**
     * Get movies by decade
     * GET /api/statistics/movies-by-decade
     */
    @GetMapping("/movies-by-decade")
    public ResponseEntity<Map<String, Object>> getMoviesByDecade() {
        Map<String, Object> data = statisticsService.getMoviesByDecade();
        return ResponseEntity.ok(data);
    }

    /**
     * Get database statistics (Admin only)
     * GET /api/statistics/database-stats
     */
    @GetMapping("/database-stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getDatabaseStats() {
        Map<String, Object> stats = statisticsService.getDatabaseStats();
        return ResponseEntity.ok(stats);
    }
}
