import React from 'react';

function WorkoutList({ workouts, isLoading, onWorkoutDeleted }) {

  const handleDelete = async (workoutId) => {
    if (!window.confirm('Are you sure you want to delete this workout?')) {
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/FitLogBackend/workouts?action=delete&id=${workoutId}`, {
        method: 'POST',
        credentials: 'include',
      });

      if (response.ok) {
        alert('Workout deleted successfully!');
        onWorkoutDeleted();
      } else {

        const errorData = await response.json();
        alert(`Failed to delete workout: ${errorData.error}`);
      }
    } catch (error) {
      console.error('Error deleting workout:', error);
      alert('An error occurred while deleting.');
    }
  };

  if (isLoading) {
    return <p className="p-8 text-center text-gray-400">Loading workouts...</p>;
  }

  if (!workouts || workouts.length === 0) {
    return <p className="p-8 text-center text-gray-400">You haven't logged any workouts yet.</p>;
  }

  return (
    <table className="w-full text-left">
      <thead className="bg-gray-700">
        <tr>
          <th className="p-4 font-semibold">Date</th>
          <th className="p-4 font-semibold">Workout Type</th>
          <th className="p-4 font-semibold">Duration (mins)</th>
          <th className="p-4 font-semibold">Calories Burned</th>
          <th className="p-4 font-semibold">Actions</th>
        </tr>
      </thead>
      <tbody>
        {workouts.map(workout => (
          <tr key={workout.id} className="border-b border-gray-700 hover:bg-gray-600">
            <td className="p-4">{new Date(workout.logDate).toLocaleDateString()}</td>
            <td className="p-4">{workout.workoutType}</td>
            <td className="p-4">{workout.durationMinutes}</td>
            <td className="p-4">{workout.caloriesBurned}</td>
            <td className="p-4">

              <button
                onClick={() => handleDelete(workout.id)}
                className="font-medium text-red-500 hover:underline"
              >
                Delete
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

export default WorkoutList;