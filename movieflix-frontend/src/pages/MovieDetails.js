import React, { useState, useEffect, useCallback } from 'react';
import { useParams, Link } from 'react-router-dom';
import { movieService } from '../services/movieService';
import Loading from '../components/common/Loading';
import ErrorMessage from '../components/common/ErrorMessage';
import './MovieDetails.css';

const MovieDetails = () => {
  const { id } = useParams();
  const [movie, setMovie] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadMovieDetails = useCallback(async () => {
    if (!id) return;

    setLoading(true);
    setError(null);

    try {
      let movieData;
      if (id.match(/^[0-9a-fA-F]{24}$/)) {
        // MongoDB ObjectId format
        movieData = await movieService.getMovieById(id);
      } else {
        // Assume TMDB ID
        movieData = await movieService.getMovieByTmdbId(parseInt(id));
      }
      
      setMovie(movieData);
    } catch (err) {
      console.error('Error loading movie details:', err);
      setError('Failed to load movie details. Please try again.');
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    loadMovieDetails();
  }, [loadMovieDetails]);

  const formatRuntime = (minutes) => {
    if (!minutes) return 'N/A';
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return hours > 0 ? `${hours}h ${mins}m` : `${mins}m`;
  };

  const formatCurrency = (amount) => {
    if (!amount) return 'N/A';
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  if (loading) return <Loading message="Loading movie details..." />;
  if (error) return <ErrorMessage message={error} onRetry={loadMovieDetails} />;
  if (!movie) return <ErrorMessage message="Movie not found" />;

  return (
    <div className="movie-details">
      <div className="movie-hero">
        {movie.backdrop && (
          <div 
            className="hero-backdrop"
            style={{ backgroundImage: `url(${movie.backdrop})` }}
          />
        )}
        
        <div className="hero-content">
          <div className="movie-poster-large">
            {movie.poster ? (
              <img 
                src={movie.poster} 
                alt={`${movie.title} poster`}
                onError={(e) => {
                  e.target.style.display = 'none';
                }}
              />
            ) : (
              <div className="poster-placeholder-large">🎬</div>
            )}
          </div>

          <div className="movie-info-main">
            <h1 className="movie-title-large">{movie.title}</h1>
            
            <div className="movie-meta-large">
              {movie.year && <span>📅 {movie.year}</span>}
              {movie.runtime && <span>⏱️ {formatRuntime(movie.runtime)}</span>}
              {movie.language && <span>🗣️ {movie.language.toUpperCase()}</span>}
            </div>

            {movie.rating?.tmdb && (
              <div className="rating-section">
                <div className="rating-score">
                  ⭐ {movie.rating.tmdb.toFixed(1)}/10
                </div>
                {movie.rating.tmdbVoteCount && (
                  <span className="rating-votes">
                    ({movie.rating.tmdbVoteCount.toLocaleString()} votes)
                  </span>
                )}
              </div>
            )}

            {movie.genre && movie.genre.length > 0 && (
              <div className="genres-section">
                {movie.genre.map((genre, index) => (
                  <span key={index} className="genre-tag-large">
                    {genre}
                  </span>
                ))}
              </div>
            )}

            {movie.plot && (
              <div className="plot-section">
                <p>{movie.plot}</p>
              </div>
            )}
          </div>
        </div>
      </div>

      <div className="movie-details-content">
        <div className="details-grid">
          {/* Cast & Crew */}
          <div className="details-section">
            <h3>🎭 Cast & Crew</h3>
            {movie.director && movie.director.length > 0 && (
              <div className="crew-item">
                <strong>Director{movie.director.length > 1 ? 's' : ''}:</strong>
                <span>{movie.director.join(', ')}</span>
              </div>
            )}
            
            {movie.actors && movie.actors.length > 0 && (
              <div className="crew-item">
                <strong>Cast:</strong>
                <span>{movie.actors.join(', ')}</span>
              </div>
            )}
          </div>

          {/* Technical Details */}
          <div className="details-section">
            <h3>🔧 Technical Details</h3>
            <div className="tech-details">
              <div className="tech-item">
                <strong>Release Date:</strong>
                <span>{formatDate(movie.releaseDate)}</span>
              </div>
              <div className="tech-item">
                <strong>Runtime:</strong>
                <span>{formatRuntime(movie.runtime)}</span>
              </div>
              <div className="tech-item">
                <strong>Language:</strong>
                <span>{movie.language || 'N/A'}</span>
              </div>
              <div className="tech-item">
                <strong>Status:</strong>
                <span>{movie.status || 'Released'}</span>
              </div>
            </div>
          </div>

          {/* Financial Information */}
          {(movie.budget || movie.revenue) && (
            <div className="details-section">
              <h3>💰 Financial Information</h3>
              <div className="financial-details">
                {movie.budget && (
                  <div className="financial-item">
                    <strong>Budget:</strong>
                    <span>{formatCurrency(movie.budget)}</span>
                  </div>
                )}
                {movie.revenue && (
                  <div className="financial-item">
                    <strong>Revenue:</strong>
                    <span>{formatCurrency(movie.revenue)}</span>
                  </div>
                )}
              </div>
            </div>
          )}

          {/* External Links */}
          <div className="details-section">
            <h3>🔗 External Links</h3>
            <div className="external-links">
              {movie.imdbId && (
                <a
                  href={`https://www.imdb.com/title/${movie.imdbId}`}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="external-link"
                >
                  🎬 View on IMDb
                </a>
              )}
              {movie.tmdbId && (
                <a
                  href={`https://www.themoviedb.org/movie/${movie.tmdbId}`}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="external-link"
                >
                  📽️ View on TMDB
                </a>
              )}
            </div>
          </div>
        </div>
      </div>

      <div className="back-to-search">
        <Link to="/" className="btn btn-primary">
          ← Back to Home
        </Link>
        <button 
          onClick={loadMovieDetails} 
          className="btn btn-outline"
          disabled={loading}
        >
          🔄 Refresh Details
        </button>
      </div>
    </div>
  );
};

export default MovieDetails;
