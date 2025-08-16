import React, { useState, useEffect } from 'react';
import { useAuth } from '../hooks/useAuth.js';
import { statisticsService } from '../services/statisticsService';
import { toast } from 'react-toastify';

import StatsCard from '../components/dashboard/StatsCard';
import PieChart from '../components/dashboard/PieChart';
import BarChart from '../components/dashboard/BarChart';
import LineChart from '../components/dashboard/LineChart';
import TopMovies from '../components/dashboard/TopMovies';
import Loading from '../components/common/Loading';
import './Dashboard.css';

const Dashboard = () => {
  const { user, hasRole } = useAuth();
  
  // State for all dashboard data
  const [dashboardData, setDashboardData] = useState({
    genreDistribution: null,
    ratingsByGenre: null,
    runtimeByYear: null,
    topMovies: [],
    moviesByDecade: null,
    dbStats: null
  });
  
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    setLoading(true);
    setError(null);

    try {
      // Load all statistics data
      const [
        genreDistribution,
        ratingsByGenre,
        runtimeByYear,
        topMovies,
        moviesByDecade,
        dbStats
      ] = await Promise.allSettled([
        statisticsService.getGenreDistribution(),
        statisticsService.getRatingsByGenre(),
        statisticsService.getRuntimeByYear(),
        statisticsService.getTopRatedMovies(10),
        statisticsService.getMoviesByDecade(),
        hasRole('admin') ? statisticsService.getDatabaseStats() : Promise.resolve(null)
      ]);

      setDashboardData({
        genreDistribution: genreDistribution.status === 'fulfilled' ? genreDistribution.value : null,
        ratingsByGenre: ratingsByGenre.status === 'fulfilled' ? ratingsByGenre.value : null,
        runtimeByYear: runtimeByYear.status === 'fulfilled' ? runtimeByYear.value : null,
        topMovies: topMovies.status === 'fulfilled' ? topMovies.value : [],
        moviesByDecade: moviesByDecade.status === 'fulfilled' ? moviesByDecade.value : null,
        dbStats: dbStats.status === 'fulfilled' ? dbStats.value : null
      });

    } catch (err) {
      console.error('Failed to load dashboard data:', err);
      setError('Failed to load dashboard data. Please try again.');
      toast.error('Failed to load dashboard data');
    } finally {
      setLoading(false);
    }
  };

  const refreshData = () => {
    loadDashboardData();
  };

  if (loading) {
    return <Loading message="Loading dashboard..." />;
  }

  if (error) {
    return (
      <div className="dashboard-error">
        <h2>Error Loading Dashboard</h2>
        <p>{error}</p>
        <button onClick={refreshData} className="retry-btn">
          Retry
        </button>
      </div>
    );
  }

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <h1>📊 MovieFlix Dashboard</h1>
        <p>Welcome back, <strong>{user?.username}</strong>!</p>
        <button onClick={refreshData} className="refresh-btn">
          🔄 Refresh Data
        </button>
      </div>

      {/* Stats Cards */}
      {dashboardData.dbStats && (
        <div className="stats-grid">
          <StatsCard
            title="Total Movies"
            value={dashboardData.dbStats.totalMovies?.toLocaleString()}
            icon="🎬"
            color="#007bff"
          />
          <StatsCard
            title="Movies with Ratings"
            value={dashboardData.dbStats.moviesWithRating?.toLocaleString()}
            icon="⭐"
            color="#28a745"
          />
          <StatsCard
            title="Average Rating"
            value={dashboardData.dbStats.averageRating?.toFixed(1)}
            icon="📊"
            color="#ffc107"
          />
          <StatsCard
            title="Unique Genres"
            value={dashboardData.dbStats.uniqueGenres}
            icon="🎭"
            color="#dc3545"
          />
        </div>
      )}

      {/* Charts Grid */}
      <div className="charts-grid">
        {/* Genre Distribution Pie Chart */}
        <div className="chart-section">
          <PieChart
            data={dashboardData.genreDistribution}
            title="Genre Distribution"
            loading={!dashboardData.genreDistribution}
          />
        </div>

        {/* Ratings by Genre Bar Chart */}
        <div className="chart-section">
          <BarChart
            data={dashboardData.ratingsByGenre}
            title="Average Ratings by Genre"
            yAxisLabel="Average Rating"
            loading={!dashboardData.ratingsByGenre}
          />
        </div>

        {/* Runtime by Year Line Chart */}
        <div className="chart-section full-width">
          <LineChart
            data={dashboardData.runtimeByYear}
            title="Average Runtime by Year"
            yAxisLabel="Runtime (minutes)"
            loading={!dashboardData.runtimeByYear}
          />
        </div>

        {/* Movies by Decade */}
        {dashboardData.moviesByDecade && (
          <div className="chart-section">
            <BarChart
              data={dashboardData.moviesByDecade}
              title="Movies by Decade"
              yAxisLabel="Number of Movies"
              loading={!dashboardData.moviesByDecade}
            />
          </div>
        )}

        {/* Top Movies */}
        {/* <div className="chart-section">
          <TopMovies
            movies={dashboardData.topMovies}
            loading={!dashboardData.topMovies}
            title="Top 10 Rated Movies"
          />
        </div> */}
      </div>

      {/* Additional Info */}
      <div className="dashboard-footer">
        {/* <div className="info-card">
          <h3>📈 Dashboard Features</h3>
          <ul>
            <li>Real-time movie statistics</li>
            <li>Interactive charts and graphs</li>
            <li>Genre and rating analytics</li>
            <li>Top movies showcase</li>
            {hasRole('admin') && <li>Admin database statistics</li>}
          </ul>
        </div> */}

        {dashboardData.dbStats && (
          <div className="info-card">
            <h3>Database Overview</h3>
            <p><strong>Year Range:</strong> {dashboardData.dbStats.yearRange}</p>
            <p><strong>Data Quality:</strong> {
              Math.round((dashboardData.dbStats.moviesWithRating / dashboardData.dbStats.totalMovies) * 100)
            }% of movies have ratings</p>
            <p><strong>Unique Genres:</strong> {dashboardData.dbStats.uniqueGenres}</p>
            <p><strong>Average Rating:</strong> {dashboardData.dbStats.averageRating?.toFixed(1)}</p>
            <p><strong>Total Movies:</strong> {dashboardData.dbStats.totalMovies?.toLocaleString()}</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
