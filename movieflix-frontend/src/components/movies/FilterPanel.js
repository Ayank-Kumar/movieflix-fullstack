import React, { useState, useEffect } from 'react';
import { movieService } from '../../services/movieService';
import './FilterPanel.css';

const FilterPanel = ({ filters, onFiltersChange }) => {
  const [genres, setGenres] = useState([]);
  const [yearRange, setYearRange] = useState({ minYear: 1900, maxYear: 2024 });
  const [isOpen, setIsOpen] = useState(false);

  useEffect(() => {
    loadGenres();
    loadYearRange();
  }, []);

  const loadGenres = async () => {
    try {
      const genreList = await movieService.getGenres();
      setGenres(genreList);
    } catch (error) {
      console.error('Failed to load genres:', error);
    }
  };

  const loadYearRange = async () => {
    try {
      const range = await movieService.getYears();
      setYearRange(range);
    } catch (error) {
      console.error('Failed to load year range:', error);
    }
  };

  const handleFilterChange = (key, value) => {
    onFiltersChange({ [key]: value });
  };

  const clearFilters = () => {
    onFiltersChange({
      genre: '',
      year: null,
      sortBy: 'title',
      sortDirection: 'asc'
    });
  };

  const hasActiveFilters = filters.genre || filters.year || 
    filters.sortBy !== 'title' || filters.sortDirection !== 'asc';

  return (
    <div className="filter-panel">
      <div className="filter-header">
        <button 
          className="filter-toggle"
          onClick={() => setIsOpen(!isOpen)}
        >
          🔧 Filters {hasActiveFilters && <span className="active-indicator">●</span>}
        </button>
        
        {hasActiveFilters && (
          <button 
            className="clear-filters"
            onClick={clearFilters}
          >
            Clear All
          </button>
        )}
      </div>

      <div className={`filter-content ${isOpen ? 'open' : ''}`}>
        {/* Genre Filter */}
        <div className="filter-group">
          <label>Genre</label>
          <select
            value={filters.genre || ''}
            onChange={(e) => handleFilterChange('genre', e.target.value)}
          >
            <option value="">All Genres</option>
            {genres.map((genre) => (
              <option key={genre} value={genre}>
                {genre}
              </option>
            ))}
          </select>
        </div>

        {/* Year Filter */}
        <div className="filter-group">
          <label>Year</label>
          <select
            value={filters.year || ''}
            onChange={(e) => handleFilterChange('year', e.target.value ? parseInt(e.target.value) : null)}
          >
            <option value="">All Years</option>
            {Array.from(
              { length: yearRange.maxYear - yearRange.minYear + 1 },
              (_, i) => yearRange.maxYear - i
            ).map((year) => (
              <option key={year} value={year}>
                {year}
              </option>
            ))}
          </select>
        </div>

        {/* Sort By */}
        <div className="filter-group">
          <label>Sort By</label>
          <select
            value={filters.sortBy}
            onChange={(e) => handleFilterChange('sortBy', e.target.value)}
          >
            <option value="title">Title</option>
            <option value="year">Year</option>
            <option value="rating">Rating</option>
            <option value="runtime">Runtime</option>
          </select>
        </div>

        {/* Sort Direction */}
        <div className="filter-group">
          <label>Order</label>
          <select
            value={filters.sortDirection}
            onChange={(e) => handleFilterChange('sortDirection', e.target.value)}
          >
            <option value="asc">Ascending</option>
            <option value="desc">Descending</option>
          </select>
        </div>
      </div>
    </div>
  );
};

export default FilterPanel;
