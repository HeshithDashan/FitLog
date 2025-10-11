import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom'; 
import WorkoutList from '../components/WorkoutList.jsx'; 
import AddWorkoutForm from '../components/AddWorkoutForm.jsx';
import WeeklyCalorieChart from '../components/WeeklyCalorieChart.jsx'; 
import toast from 'react-hot-toast';

function DashboardPage() { 
  const [workouts, setWorkouts] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [editingWorkout, setEditingWorkout] = useState(null);
  const navigate = useNavigate(); 

 
  const handleLogout = async () => {
    try {
      const response = await fetch('http://localhost:8080/FitLogBackend/logout', {
        method: 'POST',
        credentials: 'include', 
      });

      if (response.ok) {
        toast.success('Logged out successfully!');
        navigate('/login');
      } else {
        toast.error('Logout failed. Please try again.');
      }
    } catch (error) {
      console.error('Logout error:', error);
      toast.error('An error occurred during logout.');
    }
  };

  const fetchWorkouts = async () => {
    try {
      setIsLoading(true);
      const response = await fetch('http://localhost:8080/FitLogBackend/workouts', {
        credentials: 'include'
      });
      if (response.ok) {
        const data = await response.json();
        setWorkouts(data);
      } else {
        setWorkouts([]); 
      }
    } catch (error) {
      console.error("Could not fetch workouts:", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchWorkouts();
  }, []);

  const handleEditClick = (workout) => {
    setEditingWorkout(workout);
    window.scrollTo(0, 0);
  };
  
  const handleActionComplete = () => {
    setEditingWorkout(null);
    fetchWorkouts();
  };

  return (
    <div className="bg-gray-900 text-white min-h-screen p-4 md:p-8">
      <div className="max-w-4xl mx-auto">

        <header className="flex justify-between items-center mb-8">
          <h1 className="text-4xl font-bold text-sky-400">
            FitLog Dashboard
          </h1>
          <button
            onClick={handleLogout}
            className="bg-red-600 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
          >
            Logout
          </button>
        </header>
        
        <WeeklyCalorieChart />

        <AddWorkoutForm 
          workoutToEdit={editingWorkout} 
          onActionComplete={handleActionComplete} 
        />

        <WorkoutList 
          workouts={workouts} 
          isLoading={isLoading} 
          onWorkoutDeleted={fetchWorkouts} 
          onEditClick={handleEditClick}
        />

      </div>
    </div>
  );
}

export default DashboardPage;