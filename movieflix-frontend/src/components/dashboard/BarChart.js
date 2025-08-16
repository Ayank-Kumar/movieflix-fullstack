import React from 'react';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import './BarChart.css';

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

const BarChart = ({ data, title = "Bar Chart", yAxisLabel = "Values", loading = false }) => {
  const chartData = {
    labels: data?.labels || [],
    datasets: [
      {
        label: yAxisLabel,
        data: data?.data || [],
        backgroundColor: 'rgba(54, 162, 235, 0.5)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 1,
      }
    ]
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top',
      },
      tooltip: {
        callbacks: {
          label: function(context) {
            return `${context.dataset.label}: ${context.parsed.y.toFixed(2)}`;
          }
        }
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        title: {
          display: true,
          text: yAxisLabel
        }
      },
      x: {
        title: {
          display: true,
          text: 'Categories'
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
        <Bar data={chartData} options={options} />
      </div>
    </div>
  );
};

export default BarChart;
