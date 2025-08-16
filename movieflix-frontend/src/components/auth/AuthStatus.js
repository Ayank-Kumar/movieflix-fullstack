import React from 'react';
import { useAuth } from '../../hooks/useAuth';
import './AuthStatus.css';

const AuthStatus = () => {
  const { isAuthenticated, user, loading } = useAuth();

  if (loading) {
    return (
      <div className="auth-status loading">
        <span>Checking authentication...</span>
      </div>
    );
  }

  if (!isAuthenticated) {
    return (
      <div className="auth-status unauthenticated">
        <span>Not logged in</span>
      </div>
    );
  }

  return (
    <div className="auth-status authenticated">
      <span>Logged in as <strong>{user?.username}</strong></span>
      <div className="user-roles">
        {user?.roles?.map((role, index) => (
          <span key={index} className="role-badge">
            {role}
          </span>
        ))}
      </div>
    </div>
  );
};

export default AuthStatus;
