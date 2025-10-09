import React, { useState, useEffect } from 'react';

function App() {
  const [workouts, setWorkouts] = useState([]);

  useEffect(() => {
    const fetchWorkouts = async () => {
      try {
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
      }
    };
    fetchWorkouts();
  }, []);

  return (
    <div className="bg-gray-900 text-white min-h-screen p-8">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-4xl font-bold text-center mb-8 text-sky-400">
          My Workouts
        </h1>

        <div className="bg-gray-800 rounded-lg shadow-lg overflow-hidden">
          <table className="w-full text-left">
            <thead className="bg-gray-700">
              <tr>
                <th className="p-4 font-semibold">Date</th>
                <th className="p-4 font-semibold">Workout Type</th>
                <th className="p-4 font-semibold">Duration (mins)</th>
                <th className="p-4 font-semibold">Calories Burned</th>
              </tr>
            </thead>
            <tbody>
              {workouts.length > 0 ? (
                workouts.map(workout => (
                  <tr key={workout.id} className="border-b border-gray-700 hover:bg-gray-600">
                    <td className="p-4">{new Date(workout.logDate).toLocaleDateString()}</td>
                    <td className="p-4">{workout.workoutType}</td>
                    <td className="p-4">{workout.durationMinutes}</td>
                    <td className="p-4">{workout.caloriesBurned}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4" className="text-center p-8 text-gray-400">
                    Loading workouts or no workouts found...
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default App;