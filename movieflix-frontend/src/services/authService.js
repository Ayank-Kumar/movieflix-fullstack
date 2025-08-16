import api from './api';
import Cookies from 'js-cookie';

export const authService = {
  // Login user
  login: async (credentials) => {
    const response = await api.post('/auth/login', credentials);
    const { token, username, roles, expiresIn } = response.data;
    
    // Store token and user info in cookies
    const expirationDate = new Date(Date.now() + expiresIn * 1000);
    Cookies.set('token', token, { expires: expirationDate });
    Cookies.set('user', JSON.stringify({ username, roles }), { expires: expirationDate });
    
    return response.data;
  },

  // Logout user
  logout: () => {
    Cookies.remove('token');
    Cookies.remove('user');
  },

  // Get current user
  getCurrentUser: () => {
    const userStr = Cookies.get('user');
    return userStr ? JSON.parse(userStr) : null;
  },

  // Check if user is authenticated
  isAuthenticated: () => {
    return !!Cookies.get('token');
  },

  // Check if user has specific role
  hasRole: (role) => {
    const user = authService.getCurrentUser();
    return user?.roles?.includes(role.toUpperCase()) || false;
  },

  // Check authentication status
  checkAuth: async () => {
    try {
      const response = await api.get('/auth/me');
      return response.data;
    } catch (error) {
      authService.logout();
      throw error;
    }
  },
};
