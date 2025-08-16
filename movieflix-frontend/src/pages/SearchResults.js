import React, { useState, useEffect, useCallback } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { movieService } from '../services/movieService';
import { toast } from 'react-toastify';

import MovieCard from '../components/movies/MovieCard';
import FilterPanel from '../components/movies/FilterPanel';
import Pagination from '../components/movies/Pagination';
import Loading from '../components/common/Loading';
import ErrorMessage from '../components/common/ErrorMessage';
import './SearchResults.css';

const SearchResults = () => {
  const location = useLocation();
  const navigate = useNavigate();
  
  // State management
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  
  // Search parameters from URL and filters
  const [searchParams, setSearchParams] = useState({
    query: '',
    page: 1,
    size: 20,
    genre: '',
    year: null,
    sortBy: 'title',
    sortDirection: 'asc'
  });

  // Extract query from URL
  useEffect(() => {
    const urlParams = new URLSearchParams(location.search);
    const query = urlParams.get('q') || '';
    const page = parseInt(urlParams.get('page')) || 1;
    const genre = urlParams.get('genre') || '';
    const year = urlParams.get('year') ? parseInt(urlParams.get('year')) : null;
    const sortBy = urlParams.get('sortBy') || 'title';
    const sortDirection = urlParams.get('sortDirection') || 'asc';
    
    setSearchParams({ 
      query, 
      page, 
      size: 20, 
      genre, 
      year, 
      sortBy, 
      sortDirection 
    });
  }, [location.search]);

  // Search movies when params change
  const searchMovies = useCallback(async () => {
    if (!searchParams.query.trim()) return;

    setLoading(true);
    setError(null);

    try {
      const params = {
        search: searchParams.query,
        page: searchParams.page,
        size: searchParams.size,
        ...(searchParams.genre && { genre: searchParams.genre }),
        ...(searchParams.year && { year: searchParams.year }),
        sortBy: searchParams.sortBy,
        sortDirection: searchParams.sortDirection
      };

      const response = await movieService.searchMovies(params);
      
      // Handle different response formats
      if (response.movies) {
        setMovies(response.movies);
        setTotalPages(response.totalPages || 0);
        setTotalElements(response.totalElements || 0);
      } else if (Array.isArray(response)) {
        setMovies(response);
        setTotalPages(1);
        setTotalElements(response.length);
      } else {
        setMovies([]);
        setTotalPages(0);
        setTotalElements(0);
      }
      
      if (movies.length === 0 && searchParams.query) {
        toast.info('No movies found for your search criteria.');
      }
    } catch (err) {
      console.error('Search error:', err);
      setError('Failed to search movies. Please try again.');
      setMovies([]);
      setTotalPages(0);
      setTotalElements(0);
    } finally {
      setLoading(false);
    }
  }, [searchParams]);

  useEffect(() => {
    if (searchParams.query) {
      searchMovies();
    }
  }, [searchMovies]);

  const updateSearchParams = (newParams) => {
    const updatedParams = { ...searchParams, ...newParams, page: 1 };
    setSearchParams(updatedParams);
    updateURL(updatedParams);
  };

  const handlePageChange = (newPage) => {
    const updatedParams = { ...searchParams, page: newPage };
    setSearchParams(updatedParams);
    updateURL(updatedParams);
    
    // Scroll to top
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const updateURL = (params) => {
    const urlParams = new URLSearchParams();
    urlParams.set('q', params.query);
    urlParams.set('page', params.page.toString());
    if (params.genre) urlParams.set('genre', params.genre);
    if (params.year) urlParams.set('year', params.year.toString());
    if (params.sortBy !== 'title') urlParams.set('sortBy', params.sortBy);
    if (params.sortDirection !== 'asc') urlParams.set('sortDirection', params.sortDirection);
    
    navigate(`/search?${urlParams.toString()}`, { replace: true });
  };

  const handleRetry = () => {
    searchMovies();
  };

  if (!searchParams.query) {
    return (
      <div className="search-results">
        <div className="container">
          <div className="no-search">
            <div className="no-search-icon">🔍</div>
            <h2>No Search Query</h2>
            <p>Please use the search bar to find movies.</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="search-results">
      <div className="container">
        <div className="search-header">
          <h1>Search Results</h1>
          <div className="search-info">
            <p>
              <span className="search-query">"{searchParams.query}"</span>
              {searchParams.genre && <span className="filter-info">in {searchParams.genre}</span>}
              {searchParams.year && <span className="filter-info">from {searchParams.year}</span>}
            </p>
            {totalElements > 0 && (
              <p className="results-count">
                Found <strong>{totalElements.toLocaleString()}</strong> results
              </p>
            )}
          </div>
        </div>

        <div className="search-content">
          {/* Filter Panel */}
          <div className="filters-sidebar">
            <FilterPanel 
              filters={searchParams}
              onFiltersChange={updateSearchParams}
            />
          </div>

          {/* Results Section */}
          <div className="results-section">
            {loading && (
              <div className="loading-container">
                <Loading message="Searching movies..." variant="dots" />
              </div>
            )}
            
            {error && (
              <ErrorMessage 
                message={error} 
                onRetry={handleRetry}
              />
            )}
            
            {!loading && !error && movies.length > 0 && (
              <>
                <div className="results-header">
                  <div className="results-info">
                    <span>
                      Page {searchParams.page} of {totalPages} 
                      ({totalElements.toLocaleString()} total)
                    </span>
                  </div>
                  <div className="view-options">
                    <span className="sort-info">
                      Sorted by {searchParams.sortBy} ({searchParams.sortDirection})
                    </span>
                  </div>
                </div>

                <div className="results-grid">
                  {movies.map((movie) => (
                    <MovieCard 
                      key={movie.id || movie.tmdbId || Math.random()} 
                      movie={movie} 
                    />
                  ))}
                </div>
                
                {totalPages > 1 && (
                  <Pagination 
                    currentPage={searchParams.page}
                    totalPages={totalPages}
                    totalElements={totalElements}
                    onPageChange={handlePageChange}
                  />
                )}
              </>
            )}
            
            {!loading && !error && movies.length === 0 && searchParams.query && (
              <div className="no-results">
                <div className="no-results-icon">🎬</div>
                <h3>No movies found</h3>
                <p>Try adjusting your search criteria or filters.</p>
                <div className="no-results-suggestions">
                  <h4>Suggestions:</h4>
                  <ul>
                    <li>Check your spelling</li>
                    <li>Try different keywords</li>
                    <li>Remove filters to broaden your search</li>
                    <li>Search for popular movies like "Inception", "Avengers", or "Batman"</li>
                  </ul>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default SearchResults;
