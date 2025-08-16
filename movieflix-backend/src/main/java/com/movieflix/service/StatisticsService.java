package com.movieflix.service;

import com.movieflix.model.Movie;
import com.movieflix.repository.MovieRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final MovieRepository movieRepository;
    private final MongoTemplate mongoTemplate;

    public StatisticsService(MovieRepository movieRepository, MongoTemplate mongoTemplate) {
        this.movieRepository = movieRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Get genre distribution for pie chart
     */
    public Map<String, Object> getGenreDistribution() {
        List<Movie> movies = movieRepository.findAll();

        Map<String, Long> genreCount = movies.stream()
                .filter(movie -> movie.getGenre() != null)
                .flatMap(movie -> movie.getGenre().stream())
                .collect(Collectors.groupingBy(
                        genre -> genre,
                        Collectors.counting()
                ));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("labels", new ArrayList<>(genreCount.keySet()));
        result.put("data", new ArrayList<>(genreCount.values()));
        result.put("total", genreCount.values().stream().mapToLong(Long::longValue).sum());

        return result;
    }

    /**
     * Get average ratings by genre for bar chart
     */
    public Map<String, Object> getAverageRatingsByGenre() {
        List<Movie> movies = movieRepository.findAll();

        Map<String, List<Double>> genreRatings = new HashMap<>();

        for (Movie movie : movies) {
            if (movie.getGenre() != null && movie.getRating() != null && movie.getRating().getTmdb() != null) {
                for (String genre : movie.getGenre()) {
                    genreRatings.computeIfAbsent(genre, k -> new ArrayList<>())
                            .add(movie.getRating().getTmdb());
                }
            }
        }

        Map<String, Double> averageRatings = genreRatings.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .mapToDouble(Double::doubleValue)
                                .average()
                                .orElse(0.0)
                ));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("labels", new ArrayList<>(averageRatings.keySet()));
        result.put("data", new ArrayList<>(averageRatings.values()));

        return result;
    }

    /**
     * Get average runtime by year for line chart
     */
    public Map<String, Object> getAverageRuntimeByYear() {
        List<Movie> movies = movieRepository.findAll();

        Map<Integer, List<Integer>> yearRuntimes = movies.stream()
                .filter(movie -> movie.getYear() != null && movie.getRuntime() != null && movie.getRuntime() > 0)
                .collect(Collectors.groupingBy(
                        Movie::getYear,
                        Collectors.mapping(Movie::getRuntime, Collectors.toList())
                ));

        Map<Integer, Double> averageRuntimes = yearRuntimes.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .mapToInt(Integer::intValue)
                                .average()
                                .orElse(0.0)
                ));

        // Sort by year
        List<Integer> sortedYears = averageRuntimes.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        List<Double> sortedRuntimes = sortedYears.stream()
                .map(averageRuntimes::get)
                .collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("labels", sortedYears);
        result.put("data", sortedRuntimes);

        return result;
    }

    /**
     * Get top rated movies
     */
    public List<Movie> getTopRatedMovies(int limit) {
        return movieRepository.findAll().stream()
                .filter(movie -> movie.getRating() != null && movie.getRating().getTmdb() != null)
                .sorted((m1, m2) -> Double.compare(
                        m2.getRating().getTmdb(),
                        m1.getRating().getTmdb()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get movies by decade
     */
    public Map<String, Object> getMoviesByDecade() {
        List<Movie> movies = movieRepository.findAll();

        Map<String, Long> decadeCount = movies.stream()
                .filter(movie -> movie.getYear() != null)
                .collect(Collectors.groupingBy(
                        movie -> (movie.getYear() / 10) * 10 + "s",
                        Collectors.counting()
                ));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("labels", new ArrayList<>(decadeCount.keySet()));
        result.put("data", new ArrayList<>(decadeCount.values()));

        return result;
    }

    /**
     * Get database statistics
     */
    public Map<String, Object> getDatabaseStats() {
        long totalMovies = movieRepository.count();

        List<Movie> movies = movieRepository.findAll();

        long moviesWithRating = movies.stream()
                .filter(movie -> movie.getRating() != null && movie.getRating().getTmdb() != null)
                .count();

        OptionalDouble avgRating = movies.stream()
                .filter(movie -> movie.getRating() != null && movie.getRating().getTmdb() != null)
                .mapToDouble(movie -> movie.getRating().getTmdb())
                .average();

        long uniqueGenres = movies.stream()
                .filter(movie -> movie.getGenre() != null)
                .flatMap(movie -> movie.getGenre().stream())
                .distinct()
                .count();

        OptionalInt minYear = movies.stream()
                .filter(movie -> movie.getYear() != null)
                .mapToInt(Movie::getYear)
                .min();

        OptionalInt maxYear = movies.stream()
                .filter(movie -> movie.getYear() != null)
                .mapToInt(Movie::getYear)
                .max();

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalMovies", totalMovies);
        stats.put("moviesWithRating", moviesWithRating);
        stats.put("averageRating", avgRating.orElse(0.0));
        stats.put("uniqueGenres", uniqueGenres);
        stats.put("yearRange", minYear.isPresent() && maxYear.isPresent() ?
                minYear.getAsInt() + " - " + maxYear.getAsInt() : "N/A");

        return stats;
    }
}
