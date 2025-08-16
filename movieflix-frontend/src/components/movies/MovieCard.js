import React from 'react';
import { Link } from 'react-router-dom';
import './MovieCard.css';

const MovieCard = ({ movie }) => {
  const {
    id,
    tmdbId,
    title,
    year,
    runtime,
    genre,
    rating,
    poster,
    plot,
    fromCache
  } = movie;

  const formatRuntime = (minutes) => {
    if (!minutes) return 'N/A';
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return hours > 0 ? `${hours}h ${mins}m` : `${mins}m`;
  };

  const getRatingColor = (rating) => {
    if (!rating) return '#666';
    if (rating >= 8) return '#4CAF50'; // Green
    if (rating >= 6) return '#FF9800'; // Orange
    return '#f44336'; // Red
  };

  return (
    <div className="movie-card">
      {fromCache && (
        <div className="cache-indicator" title="Loaded from cache">
          💾
        </div>
      )}
      
      <Link to={`/movie/${id || tmdbId}`} className="movie-link">
        <div className="movie-poster">
          {poster ? (
            <img 
              src={poster} 
              alt={`${title} poster`}
              onError={(e) => {
                e.target.src = '/placeholder-movie.jpg';
                e.target.onerror = null;
              }}
            />
          ) : (
            <div className="poster-placeholder">
              🎬
            </div>
          )}
          
          {rating?.tmdb && (
            <div 
              className="rating-badge"
              style={{ backgroundColor: getRatingColor(rating.tmdb) }}
            >
              ⭐ {rating.tmdb.toFixed(1)}
            </div>
          )}
        </div>

        <div className="movie-info">
          <h3 className="movie-title" title={title}>
            {title}
          </h3>
          
          <div className="movie-meta">
            {year && <span className="movie-year">{year}</span>}
            {runtime && <span className="movie-runtime">{formatRuntime(runtime)}</span>}
          </div>

          {genre && genre.length > 0 && (
            <div className="movie-genres">
              {genre.slice(0, 3).map((g, index) => (
                <span key={index} className="genre-tag">
                  {g}
                </span>
              ))}
              {genre.length > 3 && (
                <span className="genre-more">+{genre.length - 3}</span>
              )}
            </div>
          )}

          {plot && (
            <p className="movie-plot">
              {plot.length > 120 ? `${plot.substring(0, 120)}...` : plot}
            </p>
          )}
        </div>
      </Link>
    </div>
  );
};

export default MovieCard;
