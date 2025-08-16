package com.movieflix.repository;

import com.movieflix.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {

    // Find by TMDB ID (for cache checking)
    Optional<Movie> findByTmdbId(Integer tmdbId);

    // Check if movie exists by TMDB ID
    boolean existsByTmdbId(Integer tmdbId);

    // Search movies by title (case insensitive)
    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // Find movies by genre
    List<Movie> findByGenreContainingIgnoreCase(String genre);

    // Find movies by year range
    List<Movie> findByYearBetween(Integer startYear, Integer endYear);

    // Find movies by year
    List<Movie> findByYear(Integer year);

    // Find movies with rating above threshold
    @Query("{ 'rating.tmdb': { $gte: ?0 } }")
    List<Movie> findByRatingAbove(Double minRating);

    // Find expired cache entries (for cleanup)
    @Query("{ 'cacheExpiry': { $lt: ?0 } }")
    List<Movie> findExpiredCacheEntries(Date currentTime);

    // Delete expired cache entries
    @Query(value = "{ 'cacheExpiry': { $lt: ?0 } }", delete = true)
    void deleteExpiredCacheEntries(Date currentTime);

    // Custom aggregation queries (we'll add more complex ones later)
    @Query("{ $group: { _id: '$genre', count: { $sum: 1 } } }")
    List<Object> getGenreDistribution();

    // Find recent movies (last 30 days in cache)
    @Query("{ 'createdAt': { $gte: ?0 } }")
    List<Movie> findRecentMovies(Date since);
}
