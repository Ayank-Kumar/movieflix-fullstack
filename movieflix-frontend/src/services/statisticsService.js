import api from './api';

export const statisticsService = {
  // Get genre distribution for pie chart
  getGenreDistribution: async () => {
    const response = await api.get('/statistics/genre-distribution');
    return response.data;
  },

  // Get average ratings by genre for bar chart
  getRatingsByGenre: async () => {
    const response = await api.get('/statistics/ratings-by-genre');
    console.log('Ratings by genre:', response.data);
    return response.data;
  },

  // Get average runtime by year for line chart
  getRuntimeByYear: async () => {
    const response = await api.get('/statistics/runtime-by-year');
    console.log('Runtime by year:', response.data);
    return response.data;
  },

  // Get top rated movies
  getTopRatedMovies: async (limit = 10) => {
    const response = await api.get(`/statistics/top-rated?limit=${limit}`);
    return response.data;
  },

  // Get movies by decade
  getMoviesByDecade: async () => {
    const response = await api.get('/statistics/movies-by-decade');
    return response.data;
  },

  // Get database stats (admin only)
  getDatabaseStats: async () => {
    const response = await api.get('/statistics/database-stats');
    return response.data;
  },
};
