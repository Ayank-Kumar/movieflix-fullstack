import React, { useState, useEffect } from 'react';
import { useAuth } from '../hooks/useAuth';
import { toast } from 'react-toastify';
import api from '../services/api';
import Loading from '../components/common/Loading';
import './AdminPanel.css';

const AdminPanel = () => {
  const { user, hasRole } = useAuth();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (hasRole('admin')) {
      loadAdminStats();
    }
  }, []);

  const loadAdminStats = async () => {
    try {
      const [cacheStats, dbStats] = await Promise.all([
        api.get('/admin/cache/stats'),
        api.get('/statistics/database-stats')
      ]);

      setStats({
        cache: cacheStats.data,
        database: dbStats.data
      });
    } catch (error) {
      console.error('Failed to load admin stats:', error);
      toast.error('Failed to load admin statistics');
    } finally {
      setLoading(false);
    }
  };

  const handleClearCache = async () => {
    if (!window.confirm('Are you sure you want to clear the entire cache?')) {
      return;
    }

    try {
      await api.delete('/admin/cache/clear');
      toast.success('Cache cleared successfully');
      loadAdminStats(); // Refresh stats
    } catch (error) {
      console.error('Failed to clear cache:', error);
      toast.error('Failed to clear cache');
    }
  };

  const handleCleanupCache = async () => {
    try {
      await api.post('/admin/cache/cleanup');
      toast.success('Cache cleanup completed');
      loadAdminStats(); // Refresh stats
    } catch (error) {
      console.error('Failed to cleanup cache:', error);
      toast.error('Failed to cleanup cache');
    }
  };

  if (!hasRole('admin')) {
    return (
      <div className="admin-panel">
        <div className="access-denied">
          <h2>Admin Access Required</h2>
          <p>You need administrator privileges to access this page.</p>
        </div>
      </div>
    );
  }

  if (loading) {
    return <Loading message="Loading admin panel..." />;
  }

  return (
    <div className="admin-panel">
      <div className="admin-header">
        <h1>🛠️ Admin Panel</h1>
        <p>Welcome, <strong>{user?.username}</strong></p>
      </div>

      <div className="admin-stats">
        <div className="stat-card">
          <h3>Cache Statistics</h3>
          <div className="stat-grid">
            <div className="stat-item">
              <span className="stat-label">Cached Movies:</span>
              <span className="stat-value">{stats?.cache?.totalCachedMovies || 0}</span>
            </div>
            <div className="stat-item">
              <span className="stat-label">Total Documents:</span>
              <span className="stat-value">{stats?.cache?.totalDocuments || 0}</span>
            </div>
          </div>
        </div>

        <div className="stat-card">
          <h3>Database Statistics</h3>
          <div className="stat-grid">
            <div className="stat-item">
              <span className="stat-label">Total Movies:</span>
              <span className="stat-value">{stats?.database?.totalMovies || 0}</span>
            </div>
            <div className="stat-item">
              <span className="stat-label">Movies with Rating:</span>
              <span className="stat-value">{stats?.database?.moviesWithRating || 0}</span>
            </div>
            <div className="stat-item">
              <span className="stat-label">Average Rating:</span>
              <span className="stat-value">
                {stats?.database?.averageRating?.toFixed(1) || 'N/A'}
              </span>
            </div>
            <div className="stat-item">
              <span className="stat-label">Unique Genres:</span>
              <span className="stat-value">{stats?.database?.uniqueGenres || 0}</span>
            </div>
            <div className="stat-item">
              <span className="stat-label">Year Range:</span>
              <span className="stat-value">{stats?.database?.yearRange || 'N/A'}</span>
            </div>
          </div>
        </div>
      </div>

      <div className="admin-actions">
        <div className="action-card">
          <h3>Cache Management</h3>
          <p>Manage the movie cache for better performance</p>
          <div className="action-buttons">
            <button 
              className="admin-btn cleanup-btn"
              onClick={handleCleanupCache}
            >
              🧹 Cleanup Expired Entries
            </button>
            <button 
              className="admin-btn danger-btn"
              onClick={handleClearCache}
            >
              🗑️ Clear All Cache
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminPanel;
