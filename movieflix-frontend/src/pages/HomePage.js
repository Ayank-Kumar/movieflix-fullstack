import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth.js';
import { movieService } from '../services/movieService';
import SearchBar from '../components/movies/SearchBar';
import MovieCard from '../components/movies/MovieCard';
import Loading from '../components/common/Loading';
import './HomePage.css';

const HomePage = () => {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();
  
  const [popularMovies, setPopularMovies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchLoading, setSearchLoading] = useState(false);

  useEffect(() => {
    loadPopularMovies();
  }, []);

  const loadPopularMovies = async () => {
    try {
      const movies = await movieService.getPopularMovies(8);
      setPopularMovies(movies);
    } catch (error) {
      console.error('Failed to load popular movies:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (query) => {
    if (!query.trim()) return;
    
    setSearchLoading(true);
    // Add a small delay to show loading state
    setTimeout(() => {
      navigate(`/search?q=${encodeURIComponent(query.trim())}`);
      setSearchLoading(false);
    }, 500);
  };

  return (
    <div className="home-page">
      {/* Hero Section */}
      <section className="hero-section">
        <div className="hero-background">
          <div className="hero-overlay"></div>
        </div>
        
        <div className="hero-content">
          <div className="hero-text">
            <h1 className="hero-title">
              🎬 Welcome to <span className="gradient-text">MovieFlix</span>
            </h1>
            <p className="hero-subtitle">
              Discover, search, and explore movies from around the world with 
              comprehensive statistics and detailed information
            </p>
          </div>
          
          <div className="hero-search">
            <SearchBar 
              onSearch={handleSearch} 
              placeholder="Search for any movie... (e.g., Inception, Avengers, Batman)"
            />
            {searchLoading && (
              <div className="search-loading">
                <Loading message="Searching movies..." size="small" variant="dots" />
              </div>
            )}
          </div>
          
          <div className="hero-actions">
            {isAuthenticated ? (
              <Link to="/dashboard" className="btn btn-primary hero-btn">
                📊 View Dashboard
              </Link>
            ) : (
              <Link to="/login" className="btn btn-primary hero-btn">
                🔐 Login to Access Dashboard
              </Link>
            )}
            <button 
              onClick={() => handleSearch('popular')} 
              className="btn btn-outline hero-btn"
            >
              🔥 Browse Popular Movies
            </button>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features-section">
        <div className="container">
          <h2 className="section-title">Why Choose MovieFlix?</h2>
          <div className="features-grid">
            <div className="feature-card">
              <div className="feature-icon">🔍</div>
              <h3>Smart Search</h3>
              <p>Find movies by title, genre, year, or rating with advanced filtering options</p>
            </div>
            
            <div className="feature-card">
              <div className="feature-icon">📊</div>
              <h3>Rich Analytics</h3>
              <p>Explore movie trends, ratings, and statistics with interactive charts</p>
            </div>
            
            <div className="feature-card">
              <div className="feature-icon">🎬</div>
              <h3>Detailed Information</h3>
              <p>Get comprehensive movie details including cast, crew, and ratings</p>
            </div>
            
            <div className="feature-card">
              <div className="feature-icon">⚡</div>
              <h3>Fast & Cached</h3>
              <p>Lightning-fast results with intelligent caching for better performance</p>
            </div>
          </div>
        </div>
      </section>

      {/* Popular Movies Section */}
      {popularMovies.length > 0 && (
        <section className="popular-section">
          <div className="container">
            <div className="section-header">
              <h2 className="section-title">Popular Movies</h2>
              <Link to="/search?q=popular" className="view-all-link">
                View All →
              </Link>
            </div>
            
            {loading ? (
              <div className="movies-grid">
                {Array(8).fill(0).map((_, index) => (
                  <div key={index} className="movie-card-skeleton">
                    <div className="skeleton-poster loading-skeleton"></div>
                    <div className="skeleton-info">
                      <div className="skeleton-title loading-skeleton"></div>
                      <div className="skeleton-meta loading-skeleton"></div>
                      <div className="skeleton-genres">
                        <div className="skeleton-genre loading-skeleton"></div>
                        <div className="skeleton-genre loading-skeleton"></div>
                      </div>
                      <div className="skeleton-plot loading-skeleton"></div>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="movies-grid">
                {popularMovies.map((movie) => (
                  <MovieCard 
                    key={movie.id || movie.tmdbId} 
                    movie={movie} 
                  />
                ))}
              </div>
            )}
          </div>
        </section>
      )}

      {/* CTA Section */}
      <section className="cta-section">
        <div className="container">
          <div className="cta-content">
            <h2>Ready to Explore Movies?</h2>
            <p>Join thousands of movie enthusiasts discovering their next favorite film</p>
            <div className="cta-actions">
              <button 
                onClick={() => handleSearch('trending')} 
                className="btn btn-primary"
              >
                🚀 Start Exploring
              </button>
              {!isAuthenticated && (
                <Link to="/login" className="btn btn-secondary">
                  📈 Access Analytics
                </Link>
              )}
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default HomePage;
