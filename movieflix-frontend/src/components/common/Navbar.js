import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import SearchBar from '../movies/SearchBar';
import './Navbar.css';

const Navbar = () => {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const handleSearch = (searchQuery) => {
    if (searchQuery.trim()) {
      navigate(`/search?q=${encodeURIComponent(searchQuery.trim())}`);
    }
  };

  return (
    <nav style={{ 
      padding: '1rem', 
      borderBottom: '1px solid #eee', 
      display: 'flex', 
      alignItems: 'center', 
      gap: '2rem' 
    }}>
      <Link to="/" style={{ textDecoration: 'none', fontSize: '1.5rem' }}>
        🎬 MovieFlix
      </Link>

      <div style={{ flex: 1 }}>
        <SearchBar onSearch={handleSearch} />
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
        <Link to="/">Home</Link>
        
        {isAuthenticated ? (
          <>
            <Link to="/dashboard">Dashboard</Link>
            <span>👤 {user?.username}</span>
            <button onClick={handleLogout}>Logout</button>
          </>
        ) : (
          <Link to="/login">Login</Link>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
