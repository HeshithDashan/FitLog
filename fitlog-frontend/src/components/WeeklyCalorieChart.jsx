import React, { useState, useEffect } from 'react';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
  Title,
  Tooltip,
  Legend
);

function WeeklyCalorieChart() {
  const [chartData, setChartData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const DAILY_GOAL = 300; 

  useEffect(() => {
    const fetchChartData = async () => {
      try {
        setIsLoading(true);
        const response = await fetch('http://localhost:8080/FitLogBackend/workouts?report=weeklyCalories', {
          credentials: 'include'
        });

        if (!response.ok) {
          throw new Error('Failed to fetch chart data');
        }

        const data = await response.json();

        if (data && data.length > 0) {
          const labels = data.map(item => new Date(item.report_date).toLocaleDateString('en-US', { month: 'short', day: 'numeric' }));
          const calories = data.map(item => item.total_calories);
          const goalData = labels.map(() => DAILY_GOAL);

          setChartData({
            labels: labels,
            datasets: [
              {
                type: 'bar',
                label: 'Calories Burned',
                data: calories,
                backgroundColor: 'rgba(54, 162, 235, 0.6)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1,
              },
              {
                type: 'line',
                label: 'Daily Goal',
                data: goalData,
                borderColor: 'rgba(255, 99, 132, 1)',
                backgroundColor: 'rgba(255, 99, 132, 0.5)',
                pointRadius: 0,
                fill: false,
                tension: 0.1
              },
            ],
          });
        }
      } catch (error) {
        console.error("Error fetching chart data:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchChartData();
  }, []);


  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
        labels: {
            color: '#fff'
        }
      },
      title: {
        display: true,
        text: 'Calories Burned vs. Daily Goal',
        color: '#fff',
        font: {
            size: 18
        }
      },
    },
    scales: {
        y: {
            ticks: { color: '#ccc' }
        },
        x: {
            ticks: { color: '#ccc' }
        }
    }
  };

  if (isLoading) {
    return <div className="text-center p-4">Loading Chart...</div>;
  }

  if (!chartData) {
    return <div className="text-center p-4">Not enough data to display the chart.</div>;
  }

  return (
    <div className="bg-gray-800 p-6 rounded-lg shadow-lg mb-8">
      <Bar options={options} data={chartData} />
    </div>
  );
}

export default WeeklyCalorieChart;