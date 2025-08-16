import React, { useState } from 'react';
import { Navigate, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { toast } from 'react-toastify';
import './Login.css';

const Login = () => {
  const { login, isAuthenticated, loading } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });
  const [isLoggingIn, setIsLoggingIn] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  // Redirect to intended page after login
  const from = location.state?.from?.pathname || '/';

  // If already authenticated, redirect
  if (isAuthenticated && !loading) {
    return <Navigate to={from} replace />;
  }

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.username || !formData.password) {
      toast.error('Please enter both username and password');
      return;
    }

    setIsLoggingIn(true);

    try {
      await login({
        username: formData.username,
        password: formData.password
      });

      toast.success('Login successful!');
      navigate(from, { replace: true });
    } catch (error) {
      console.error('Login error:', error);
      
      const errorMessage = error.response?.data?.message || 
                          error.response?.data?.error ||
                          'Login failed. Please check your credentials.';
      
      toast.error(errorMessage);
    } finally {
      setIsLoggingIn(false);
    }
  };

  const handleDemoLogin = () => {
    setFormData({
      username: 'admin',
      password: 'admin123'
    });
  };

  return (
    <div className="login-page">
      <div className="login-container">
        <div className="login-header">
          <h1>🎬 MovieFlix Login</h1>
          <p>Access your movie dashboard</p>
        </div>

        <form className="login-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleInputChange}
              placeholder="Enter your username"
              disabled={isLoggingIn}
              autoComplete="username"
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <div className="password-input-container">
              <input
                type={showPassword ? 'text' : 'password'}
                id="password"
                name="password"
                value={formData.password}
                onChange={handleInputChange}
                placeholder="Enter your password"
                disabled={isLoggingIn}
                autoComplete="current-password"
              />
              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowPassword(!showPassword)}
                disabled={isLoggingIn}
              >
                {showPassword ? '👁️' : '👁️‍🗨️'}
              </button>
            </div>
          </div>

          <button
            type="submit"
            className="login-btn"
            disabled={isLoggingIn || !formData.username || !formData.password}
          >
            {isLoggingIn ? (
              <span>
                <span className="spinner"></span>
                Logging in...
              </span>
            ) : (
              'Login'
            )}
          </button>

          <div className="demo-login">
            <p>For demo purposes:</p>
            <button
              type="button"
              className="demo-btn"
              onClick={handleDemoLogin}
              disabled={isLoggingIn}
            >
              Use Demo Credentials (admin/admin123)
            </button>
          </div>
        </form>

        <div className="login-footer">
          <p>
            This is a demo application. Use the admin credentials above to access
            the dashboard and admin features.
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;
