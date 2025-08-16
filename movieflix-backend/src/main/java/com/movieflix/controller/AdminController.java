package com.movieflix.controller;

import com.movieflix.model.Movie;
import com.movieflix.service.CacheService;
import com.movieflix.service.TMDBApiService;
import com.movieflix.repository.MovieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final CacheService cacheService;
    private final MovieRepository movieRepository;
    private final TMDBApiService tmdbApiService;

    public AdminController(CacheService cacheService, MovieRepository movieRepository,
                           TMDBApiService tmdbApiService) {
        this.cacheService = cacheService;
        this.movieRepository = movieRepository;
        this.tmdbApiService = tmdbApiService;
    }

    /**
     * Get cache statistics
     * GET /api/admin/cache/stats
     */
    @GetMapping("/cache/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCachedMovies", cacheService.getCacheSize());
        stats.put("totalDocuments", movieRepository.count());

        return ResponseEntity.ok(stats);
    }

    /**
     * Clear entire cache
     * DELETE /api/admin/cache/clear
     */
    @DeleteMapping("/cache/clear")
    public ResponseEntity<Map<String, String>> clearCache() {
        cacheService.clearCache();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Cache cleared successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Force cache cleanup of expired entries
     * POST /api/admin/cache/cleanup
     */
    @PostMapping("/cache/cleanup")
    public ResponseEntity<Map<String, String>> forceCleanupCache() {
        cacheService.cleanExpiredCache();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Cache cleanup completed");

        return ResponseEntity.ok(response);
    }

    /**
     * Get all cached movies
     * GET /api/admin/movies?page=0&size=20
     */
    @GetMapping("/movies")
    public ResponseEntity<Map<String, Object>> getAllCachedMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<Movie> movies = movieRepository.findAll();
        int totalElements = movies.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        int start = page * size;
        int end = Math.min(start + size, totalElements);
        List<Movie> pageContent = movies.subList(start, end);

        Map<String, Object> response = new HashMap<>();
        response.put("content", pageContent);
        response.put("totalElements", totalElements);
        response.put("totalPages", totalPages);
        response.put("currentPage", page);
        response.put("size", size);

        return ResponseEntity.ok(response);
    }

    /**
     * Delete movie from cache
     * DELETE /api/admin/movies/{id}
     */
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Map<String, String>> deleteMovie(@PathVariable String id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Movie deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Movie not found");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Refresh movie data from TMDB
     * PUT /api/admin/movies/{tmdbId}/refresh
     */
    @PutMapping("/movies/{tmdbId}/refresh")
    public ResponseEntity<Movie> refreshMovieData(@PathVariable Integer tmdbId) {
        try {
            Movie updatedMovie = tmdbApiService.getMovieDetails(tmdbId);
            if (updatedMovie != null) {
                Movie savedMovie = cacheService.saveToCache(updatedMovie);
                return ResponseEntity.ok(savedMovie);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
