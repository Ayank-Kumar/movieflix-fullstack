import React, { useState } from 'react';
import './SearchBar.css';

const SearchBar = ({ onSearch, placeholder = "Search movies..." }) => {
  const [query, setQuery] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onSearch(query);
  };

  return (
    <form onSubmit={handleSubmit} style={{ display: 'flex', gap: '0.5rem' }}>
      <input
        type="text"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder={placeholder}
        style={{ 
          padding: '0.5rem', 
          border: '1px solid #ccc', 
          borderRadius: '4px',
          flex: 1 
        }}
      />
      <button type="submit" style={{ 
        padding: '0.5rem 1rem', 
        backgroundColor: '#007bff', 
        color: 'white', 
        border: 'none', 
        borderRadius: '4px' 
      }}>
        🔍 Search
      </button>
    </form>
  );
};

export default SearchBar;
