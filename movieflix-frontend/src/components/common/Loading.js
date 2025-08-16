import React from 'react';
import './Loading.css';

const Loading = ({ 
  message = 'Loading...', 
  size = 'medium', 
  variant = 'spinner',
  fullscreen = false 
}) => {
  const containerClass = `loading-container ${size} ${fullscreen ? 'fullscreen' : ''}`;
  
  const renderSpinner = () => (
    <div className="loading-spinner">
      <div className="spinner-ring">
        <div></div>
        <div></div>
        <div></div>
        <div></div>
      </div>
    </div>
  );

  const renderDots = () => (
    <div className="loading-dots">
      <div className="dot"></div>
      <div className="dot"></div>
      <div className="dot"></div>
    </div>
  );

  const renderPulse = () => (
    <div className="loading-pulse">
      <div className="pulse-circle"></div>
    </div>
  );

  const renderLoader = () => {
    switch (variant) {
      case 'dots':
        return renderDots();
      case 'pulse':
        return renderPulse();
      default:
        return renderSpinner();
    }
  };

  return (
    <div className={containerClass}>
      {renderLoader()}
      {message && <p className="loading-message">{message}</p>}
    </div>
  );
};

export default Loading;
