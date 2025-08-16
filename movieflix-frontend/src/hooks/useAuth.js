// src/hooks/useAuth.js
import React, { createContext, useContext, useState, useEffect } from 'react';
import { authService } from '../services/authService';
import { toast } from 'react-toastify';

// Create context
const AuthContext = createContext();

// Named export: useAuth hook
export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}

// Named export: AuthProvider component
export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const initialize = async () => {
      try {
        if (authService.isAuthenticated()) {
          const currentUser = authService.getCurrentUser();
          setUser(currentUser);
          setIsAuthenticated(true);
        }
      } catch {
        authService.logout();
      } finally {
        setLoading(false);
      }
    };
    initialize();
  }, []);

  const login = async (credentials) => {
    const response = await authService.login(credentials);
    setUser({ username: response.username, roles: response.roles });
    setIsAuthenticated(true);
    return response;
  };

  const logout = () => {
    authService.logout();
    setUser(null);
    setIsAuthenticated(false);
    toast.info('Logged out');
  };

  const hasRole = (role) => authService.hasRole(role);

  const value = { user, isAuthenticated, loading, login, logout, hasRole };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}
