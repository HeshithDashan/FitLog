import React, { useState } from 'react';

function AddWorkoutForm() {
  const [workoutType, setWorkoutType] = useState('Running');
  const [duration, setDuration] = useState('');
  const [calories, setCalories] = useState('');
  const [date, setDate] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    const workoutData = {
      workoutType,
      durationMinutes: parseInt(duration),
      caloriesBurned: parseInt(calories) || 0,
      logDate: date,
    };

    try {
      const response = await fetch('http://localhost:8080/FitLogBackend/workouts', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(workoutData),
      });

      if (response.ok) {
        alert('Workout added successfully!');
        window.location.reload();
      } else {
        alert('Failed to add workout.');
      }
    } catch (error) {
      console.error('Error submitting workout:', error);
      alert('An error occurred.');
    }
  };

  return (
    <div className="bg-gray-800 rounded-lg shadow-lg p-8 mb-8">
      <h2 className="text-2xl font-bold text-center mb-6 text-sky-400">Add a New Workout</h2>
      <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label htmlFor="workoutType" className="block mb-2 text-sm font-medium text-gray-300">Workout Type</label>
          <select id="workoutType" value={workoutType} onChange={e => setWorkoutType(e.target.value)} className="bg-gray-700 border border-gray-600 text-white text-sm rounded-lg block w-full p-2.5">
            <option value="Running">Running</option>
            <option value="Weightlifting">Weightlifting</option>
            <option value="Cycling">Cycling</option>
            <option value="Swimming">Swimming</option>
            <option value="Yoga">Yoga</option>
            <option value="Other">Other</option>
          </select>
        </div>
        <div>
          <label htmlFor="duration" className="block mb-2 text-sm font-medium text-gray-300">Duration (mins)</label>
          <input type="number" id="duration" value={duration} onChange={e => setDuration(e.target.value)} className="bg-gray-700 border border-gray-600 text-white text-sm rounded-lg block w-full p-2.5" required />
        </div>
        <div>
          <label htmlFor="calories" className="block mb-2 text-sm font-medium text-gray-300">Calories Burned</label>
          <input type="number" id="calories" value={calories} onChange={e => setCalories(e.target.value)} className="bg-gray-700 border border-gray-600 text-white text-sm rounded-lg block w-full p-2.5" />
        </div>
        <div>
          <label htmlFor="date" className="block mb-2 text-sm font-medium text-gray-300">Date</label>
          <input type="date" id="date" value={date} onChange={e => setDate(e.target.value)} className="bg-gray-700 border border-gray-600 text-white text-sm rounded-lg block w-full p-2.5" required />
        </div>
        <div className="md:col-span-2">
          <button type="submit" className="mt-4 w-full text-white bg-sky-500 hover:bg-sky-600 font-medium rounded-lg text-sm px-5 py-3 text-center">
            Add Workout Log
          </button>
        </div>
      </form>
    </div>
  );
}

export default AddWorkoutForm;