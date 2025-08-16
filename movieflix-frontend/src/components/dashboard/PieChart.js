import React from 'react';
import { Pie } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend
} from 'chart.js';
import './PieChart.css';

ChartJS.register(ArcElement, Tooltip, Legend);

const PieChart = ({ data, title = "Pie Chart", loading = false }) => {
  const chartData = {
    labels: data?.labels || [],
    datasets: [
      {
        label: 'Count',
        data: data?.data || [],
        backgroundColor: [
          '#FF6384',
          '#36A2EB',
          '#FFCE56',
          '#4BC0C0',
          '#9966FF',
          '#FF9F40',
          '#FF6384',
          '#C9CBCF',
          '#4BC0C0',
          '#36A2EB'
        ],
        borderColor: [
          '#FF6384',
          '#36A2EB',
          '#FFCE56',
          '#4BC0C0',
          '#9966FF',
          '#FF9F40',
          '#FF6384',
          '#C9CBCF',
          '#4BC0C0',
          '#36A2EB'
        ],
        borderWidth: 1,
      }
    ]
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'right',
        labels: {
          boxWidth: 12,
          font: {
            size: 12
          }
        }
      },
      tooltip: {
        callbacks: {
          label: function(context) {
            const label = context.label || '';
            const value = context.parsed;
            const total = context.dataset.data.reduce((a, b) => a + b, 0);
            const percentage = ((value / total) * 100).toFixed(1);
            return `${label}: ${value} (${percentage}%)`;
          }
        }
      }
    }
  };

  if (loading) {
    return (
      <div className="chart-container">
        <h3>{title}</h3>
        <div className="chart-loading">Loading chart...</div>
      </div>
    );
  }

  if (!data || !data.labels || data.labels.length === 0) {
    return (
      <div className="chart-container">
        <h3>{title}</h3>
        <div className="chart-no-data">No data available</div>
      </div>
    );
  }

  return (
    <div className="chart-container">
      <h3>{title}</h3>
      <div className="chart-wrapper">
        <Pie data={chartData} options={options} />
      </div>
      <div className="chart-summary">
        Total: {data.total || data.data?.reduce((a, b) => a + b, 0)}
      </div>
    </div>
  );
};

export default PieChart;
