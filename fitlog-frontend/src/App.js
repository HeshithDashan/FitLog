import React, { useState, useEffect } from 'react';
import './App.css';

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
    <div className="App">
      <header className="App-header">
        <h1>My Workouts (from API!)</h1>

        {workouts.length > 0 ? (
          <ul>
            {workouts.map(workout => (
              <li key={workout.id}>
                {workout.logDate}: {workout.workoutType} - {workout.durationMinutes} mins
              </li>
            ))}
          </ul>
        ) : (
          <p>Loading workouts or no workouts found...</p>
        )}

      </header>
    </div>
  );
}

export default App;