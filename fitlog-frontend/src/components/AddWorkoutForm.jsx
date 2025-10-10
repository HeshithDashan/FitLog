import React, { useState } from 'react';

function AddWorkoutForm({ onWorkoutAdded }) {
  const [workoutType, setWorkoutType] = useState('Running');
  const [duration, setDuration] = useState('');
  const [calories, setCalories] = useState('');
  const [date, setDate] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new URLSearchParams();
    formData.append('workoutType', workoutType);
    formData.append('durationMinutes', duration);
    formData.append('caloriesBurned', calories);
    formData.append('logDate', date);

    try {
      const response = await fetch('http://localhost:8080/FitLogBackend/workouts', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: formData.toString(),
        credentials: 'include',
      });


      if (response.ok) {

        alert('Workout added successfully!');
        setWorkoutType('Running');
        setDuration('');
        setCalories('');
        setDate('');
        onWorkoutAdded();
      } else {
        const errorData = await response.json();
        alert(`Failed to add workout: ${errorData.error || 'Unknown server error'}`);
      }
    } catch (error) {
      console.error('Error adding workout:', error);
      alert('An error occurred while adding the workout.');
    }
  };

  return (
    <div className="bg-gray-800 p-6 rounded-lg shadow-lg mb-8">
      <h2 className="text-2xl font-bold mb-4 text-white">Add a New Workout</h2>
      <form onSubmit={handleSubmit}>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label htmlFor="workout-type" className="block mb-2 text-sm font-medium text-gray-300">Workout Type</label>
            <select id="workout-type" value={workoutType} onChange={(e) => setWorkoutType(e.target.value)} className="bg-gray-700 border border-gray-600 text-white text-sm rounded-lg focus:ring-sky-500 focus:border-sky-500 block w-full p-2.5">
              <option>Running</option>
              <option>Cycling</option>
              <option>Weightlifting</option>
              <option>Swimming</option>
              <option>Yoga</option>
            </select>
          </div>
          <div>
            <label htmlFor="duration" className="block mb-2 text-sm font-medium text-gray-300">Duration (mins)</label>
            <input type="number" id="duration" value={duration} onChange={(e) => setDuration(e.target.value)} className="bg-gray-700 border border-gray-600 text-white text-sm rounded-lg focus:ring-sky-500 focus:border-sky-500 block w-full p-2.5" required />
          </div>
          <div>
            <label htmlFor="calories" className="block mb-2 text-sm font-medium text-gray-300">Calories Burned (Optional)</label>
            <input type="number" id="calories" value={calories} onChange={(e) => setCalories(e.target.value)} className="bg-gray-700 border border-gray-600 text-white text-sm rounded-lg focus:ring-sky-500 focus:border-sky-500 block w-full p-2.5" />
          </div>
          <div>
            <label htmlFor="date" className="block mb-2 text-sm font-medium text-gray-300">Date</label>
            <input type="date" id="date" value={date} onChange={(e) => setDate(e.target.value)} className="bg-gray-700 border border-gray-600 text-white text-sm rounded-lg focus:ring-sky-500 focus:border-sky-500 block w-full p-2.5" required />
          </div>
        </div>
        <button type="submit" className="mt-6 w-full text-white bg-sky-600 hover:bg-sky-700 focus:ring-4 focus:outline-none focus:ring-sky-800 font-medium rounded-lg text-sm px-5 py-2.5 text-center">
          Add Workout Log
        </button>
      </form>
    </div>
  );
}

export default AddWorkoutForm;