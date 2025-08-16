package com.movieflix.service;

import com.movieflix.model.Movie;
import com.movieflix.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CacheService {

    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

    private final MovieRepository movieRepository;

    public CacheService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Check if movie exists in cache and is not expired
     */
    public boolean isMovieInCache(Integer tmdbId) {
        return movieRepository.existsByTmdbId(tmdbId);
    }

    /**
     * Get movie from cache
     */
    public Movie getMovieFromCache(Integer tmdbId) {
        return movieRepository.findByTmdbId(tmdbId).orElse(null);
    }

    /**
     * Save or update movie in cache
     */
    public Movie saveToCache(Movie movie) {
        // Check if movie already exists
        Movie existingMovie = movieRepository.findByTmdbId(movie.getTmdbId()).orElse(null);

        if (existingMovie != null) {
            // Update existing movie
            updateExistingMovie(existingMovie, movie);
            existingMovie.refreshCache();
            return movieRepository.save(existingMovie);
        } else {
            // Save new movie
            return movieRepository.save(movie);
        }
    }

    /**
     * Update existing movie with new data
     */
    private void updateExistingMovie(Movie existing, Movie updated) {
        existing.setTitle(updated.getTitle());
        existing.setYear(updated.getYear());
        existing.setRuntime(updated.getRuntime());
        existing.setGenre(updated.getGenre());
        existing.setDirector(updated.getDirector());
        existing.setActors(updated.getActors());
        existing.setPlot(updated.getPlot());
        existing.setRating(updated.getRating());
        existing.setPoster(updated.getPoster());
        existing.setBackdrop(updated.getBackdrop());
        existing.setReleaseDate(updated.getReleaseDate());
        existing.setBudget(updated.getBudget());
        existing.setRevenue(updated.getRevenue());
        existing.setLanguage(updated.getLanguage());
        existing.setStatus(updated.getStatus());
        existing.setImdbId(updated.getImdbId());
    }

    /**
     * Clean expired cache entries (runs every hour)
     */
    @Scheduled(fixedRate = 3600000) // Every hour
    public void cleanExpiredCache() {
        try {
            Date currentTime = new Date();
            List<Movie> expiredMovies = movieRepository.findExpiredCacheEntries(currentTime);

            if (!expiredMovies.isEmpty()) {
                logger.info("Cleaning {} expired cache entries", expiredMovies.size());
                movieRepository.deleteExpiredCacheEntries(currentTime);
                logger.info("Cache cleanup completed");
            }
        } catch (Exception e) {
            logger.error("Error during cache cleanup: {}", e.getMessage());
        }
    }

    /**
     * Get cache statistics
     */
    public long getCacheSize() {
        return movieRepository.count();
    }

    /**
     * Clear entire cache (admin function)
     */
    public void clearCache() {
        logger.warn("Clearing entire movie cache");
        movieRepository.deleteAll();
    }
}
