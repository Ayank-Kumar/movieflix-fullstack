import React from 'react';
import './StatsCard.css';

const StatsCard = ({ 
  title, 
  value, 
  icon, 
  color = '#007bff', 
  subtitle = null,
  loading = false 
}) => {
  if (loading) {
    return (
      <div className="stats-card loading">
        <div className="stats-card-content">
          <div className="stats-loading">Loading...</div>
        </div>
      </div>
    );
  }

  return (
    <div className="stats-card" style={{ borderLeftColor: color }}>
      <div className="stats-card-content">
        <div className="stats-header">
          <div className="stats-icon" style={{ color }}>
            {icon}
          </div>
          <div className="stats-info">
            <h3 className="stats-title">{title}</h3>
            <div className="stats-value" style={{ color }}>
              {value}
            </div>
            {subtitle && (
              <div className="stats-subtitle">
                {subtitle}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default StatsCard;
