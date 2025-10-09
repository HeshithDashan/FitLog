import React, { useState, useEffect } from 'react';
import WorkoutList from './components/WorkoutList.jsx';
import AddWorkoutForm from './components/AddWorkoutForm.jsx';
import './index.css';

function App() {
  const [workouts, setWorkouts] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  const fetchWorkouts = async () => {
    try {
      setIsLoading(true);
      const response = await fetch('http://localhost:8080/FitLogBackend/workouts', {
        credentials: 'include'
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json();
      setWorkouts(data);
    } catch (error) {
      console.error("Could not fetch workouts:", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchWorkouts();
  }, []);

  return (
    <div className="bg-gray-900 text-white min-h-screen p-4 md:p-8">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-4xl font-bold text-center mb-8 text-sky-400">
          FitLog Dashboard
        </h1>

        <AddWorkoutForm />
        <WorkoutList workouts={workouts} isLoading={isLoading} onWorkoutDeleted={fetchWorkouts} />
      </div>
    </div>
  );
}

export default App;