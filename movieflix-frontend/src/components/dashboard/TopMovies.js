import React from 'react';
import { Link } from 'react-router-dom';
import './TopMovies.css';

const TopMovies = ({ movies, loading = false, title = "Top Rated Movies" }) => {
  if (loading) {
    return (
      <div className="top-movies">
        <h3>{title}</h3>
        <div className="loading">Loading movies...</div>
      </div>
    );
  }

  if (!movies || movies.length === 0) {
    return (
      <div className="top-movies">
        <h3>{title}</h3>
        <div className="no-movies">No movies available</div>
      </div>
    );
  }

  return (
    <div className="top-movies">
      <h3>{title}</h3>
      <div className="movies-list">
        {movies.map((movie, index) => (
          <div key={movie.id || movie.tmdbId} className="movie-item">
            <div className="movie-rank">#{index + 1}</div>
            
            <div className="movie-poster-small">
              {movie.poster ? (
                <img 
                  src={movie.poster} 
                  alt={`${movie.title} poster`}
                  onError={(e) => {
                    e.target.style.display = 'none';
                  }}
                />
              ) : (
                <div className="poster-placeholder-small">🎬</div>
              )}
            </div>

            <div className="movie-details">
              <Link 
                to={`/movie/${movie.id || movie.tmdbId}`} 
                className="movie-title"
              >
                {movie.title}
              </Link>
              
              <div className="movie-meta">
                {movie.year && <span className="year">{movie.year}</span>}
                {movie.rating?.tmdb && (
                  <span className="rating">
                    ⭐ {movie.rating.tmdb.toFixed(1)}
                  </span>
                )}
              </div>

              {movie.genre && movie.genre.length > 0 && (
                <div className="movie-genres">
                  {movie.genre.slice(0, 2).map((genre, i) => (
                    <span key={i} className="genre-tag-small">
                      {genre}
                    </span>
                  ))}
                </div>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default TopMovies;
