import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import Loading from '../common/Loading';
import './ProtectedRoute.css';

const ProtectedRoute = ({ children, requiredRole = null, fallbackMessage = null }) => {
  const { isAuthenticated, loading, hasRole, user } = useAuth();
  const location = useLocation();

  if (loading) {
    return <Loading message="Checking authentication..." />;
  }

  if (!isAuthenticated) {
    // Redirect to login page with return URL
    return (
      <Navigate 
        to="/login" 
        state={{ from: location }} 
        replace 
      />
    );
  }

  if (requiredRole && !hasRole(requiredRole)) {
    return (
      <div className="access-denied">
        <div className="access-denied-content">
          <div className="access-denied-icon">🚫</div>
          <h2>Access Denied</h2>
          <p>
            {fallbackMessage || 
             `You need ${requiredRole.toUpperCase()} privileges to access this page.`}
          </p>
          <div className="user-info">
            <p>Current user: <strong>{user?.username}</strong></p>
            <p>Your roles: <strong>{user?.roles?.join(', ') || 'None'}</strong></p>
          </div>
          <div className="access-denied-actions">
            <button onClick={() => window.history.back()}>
              ← Go Back
            </button>
          </div>
        </div>
      </div>
    );
  }

  return children;
};

export default ProtectedRoute;
