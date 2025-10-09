import React from 'react';

function WorkoutList({ workouts, isLoading }) {
  if (isLoading) {
    return <p className="p-8 text-center text-gray-400">Loading workouts...</p>;
  }

  if (!workouts || workouts.length === 0) {
    return <p className="p-8 text-center text-gray-400">You haven't logged any workouts yet.</p>;
  }

  return (
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
          {workouts.map(workout => (
            <tr key={workout.id} className="border-b border-gray-700 hover:bg-gray-600">
              <td className="p-4">{new Date(workout.logDate).toLocaleDateString()}</td>
              <td className="p-4">{workout.workoutType}</td>
              <td className="p-4">{workout.durationMinutes}</td>
              <td className="p-4">{workout.caloriesBurned}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default WorkoutList;