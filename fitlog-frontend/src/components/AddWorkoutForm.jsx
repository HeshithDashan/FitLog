import React, { useState, useEffect } from 'react';
import toast from 'react-hot-toast'; 

function AddWorkoutForm({ workoutToEdit, onActionComplete }) { 
  const [workoutType, setWorkoutType] = useState('Running');
  const [duration, setDuration] = useState('');
  const [calories, setCalories] = useState('');
  const [date, setDate] = useState('');

  useEffect(() => {
    if (workoutToEdit) {
      setWorkoutType(workoutToEdit.workoutType);
      setDuration(workoutToEdit.durationMinutes);
      setCalories(workoutToEdit.caloriesBurned);
      setDate(new Date(workoutToEdit.logDate).toISOString().split('T')[0]);
    } else {
      setWorkoutType('Running');
      setDuration('');
      setCalories('');
      setDate('');
    }
  }, [workoutToEdit]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new URLSearchParams();
    formData.append('workoutType', workoutType);
    formData.append('durationMinutes', duration);
    formData.append('caloriesBurned', calories);
    formData.append('logDate', date);
    
    let url = 'http://localhost:8080/FitLogBackend/workouts';
    let method = 'POST';

    if (workoutToEdit) {
      formData.append('id', workoutToEdit.id);
    }

    try {
      const response = await fetch(url, {
        method: method,
        body: formData.toString(),
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        credentials: 'include',
      });

      if (response.ok) {

        toast.success(workoutToEdit ? 'Workout updated successfully!' : 'Workout added successfully!');
        onActionComplete();
      } else {
        const errorData = await response.json();

        toast.error(`Failed: ${errorData.error || 'Unknown server error'}`);
      }
    } catch (error) {
      console.error('Error submitting form:', error);

      toast.error('An error occurred while submitting the form.');
    }
  };

  return (
    <div className="bg-gray-800 p-6 rounded-lg shadow-lg mb-8">
      <h2 className="text-2xl font-bold mb-4 text-white">
        {workoutToEdit ? 'Edit Workout' : 'Add a New Workout'}
      </h2>
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
          {workoutToEdit ? 'Update Workout' : 'Add Workout Log'}
        </button>
      </form>
    </div>
  );
}

export default AddWorkoutForm;