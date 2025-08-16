import api from './api';

export const movieService = {
  // Search movies with filters
  searchMovies: async (params) => {
    const response = await api.get('/movies', { params });
    return response.data;
  },

  // Advanced search with POST
  advancedSearch: async (searchRequest) => {
    const response = await api.post('/movies/search', searchRequest);
    return response.data;
  },

  // Get movie by ID
  getMovieById: async (id) => {
    const response = await api.get(`/movies/${id}`);
    return response.data;
  },

  // Get movie by TMDB ID
  getMovieByTmdbId: async (tmdbId) => {
    const response = await api.get(`/movies/tmdb/${tmdbId}`);
    return response.data;
  },

  // Get movies by genre
  getMoviesByGenre: async (genre) => {
    const response = await api.get(`/movies/genre/${genre}`);
    return response.data;
  },

  // Get movies by year
  getMoviesByYear: async (year) => {
    const response = await api.get(`/movies/year/${year}`);
    return response.data;
  },

  // Get popular movies
  getPopularMovies: async (limit = 20) => {
    const response = await api.get(`/movies/popular?limit=${limit}`);
    return response.data;
  },

  // Get available genres
  getGenres: async () => {
    const response = await api.get('/movies/genres');
    return response.data;
  },

  // Get available years
  getYears: async () => {
    const response = await api.get('/movies/years');
    return response.data;
  },
};
