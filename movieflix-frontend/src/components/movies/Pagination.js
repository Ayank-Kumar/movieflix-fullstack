import React from 'react';
import './Pagination.css';

const Pagination = ({ currentPage, totalPages, totalElements, onPageChange }) => {
  if (totalPages <= 1) return null;

  const getVisiblePages = () => {
    const delta = 2;
    const range = [];
    const rangeWithDots = [];

    for (let i = Math.max(2, currentPage - delta); 
         i <= Math.min(totalPages - 1, currentPage + delta); i++) {
      range.push(i);
    }

    if (currentPage - delta > 2) {
      rangeWithDots.push(1, '...');
    } else {
      rangeWithDots.push(1);
    }

    rangeWithDots.push(...range);

    if (currentPage + delta < totalPages - 1) {
      rangeWithDots.push('...', totalPages);
    } else if (totalPages > 1) {
      rangeWithDots.push(totalPages);
    }

    return rangeWithDots;
  };

  const visiblePages = getVisiblePages();

  return (
    <div className="pagination">
      <div className="pagination-info">
        Showing page {currentPage} of {totalPages} ({totalElements} total results)
      </div>
      
      <div className="pagination-controls">
        <button
          className="page-btn prev"
          onClick={() => onPageChange(currentPage - 1)}
          disabled={currentPage <= 1}
        >
          ← Previous
        </button>

        <div className="page-numbers">
          {visiblePages.map((page, index) => (
            <span key={index}>
              {page === '...' ? (
                <span className="page-dots">...</span>
              ) : (
                <button
                  className={`page-btn ${page === currentPage ? 'active' : ''}`}
                  onClick={() => onPageChange(page)}
                >
                  {page}
                </button>
              )}
            </span>
          ))}
        </div>

        <button
          className="page-btn next"
          onClick={() => onPageChange(currentPage + 1)}
          disabled={currentPage >= totalPages}
        >
          Next →
        </button>
      </div>
    </div>
  );
};

export default Pagination;
