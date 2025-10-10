import React, { useState } from 'react';
import toast from 'react-hot-toast';
import ConfirmDeleteModal from './ConfirmDeleteModal.jsx'; 
function WorkoutList({ workouts, isLoading, onWorkoutDeleted, onEditClick }) {

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [workoutToDelete, setWorkoutToDelete] = useState(null);

  const openConfirmationModal = (workout) => {
    setWorkoutToDelete(workout);
    setIsModalOpen(true);
  };

  const closeConfirmationModal = () => {
    setWorkoutToDelete(null);
    setIsModalOpen(false);
  };

  const handleDelete = async () => {
    if (!workoutToDelete) return;
    try {
      const response = await fetch(`http://localhost:8080/FitLogBackend/workouts?action=delete&id=${workoutToDelete.id}`, {
        method: 'POST',
        credentials: 'include',
      });
      if (response.ok) {
        toast.success('Workout deleted successfully!');
        onWorkoutDeleted();
      } else {
        const errorData = await response.json();
        toast.error(`Failed to delete workout: ${errorData.error}`);
      }
    } catch (error) {
      console.error('Error deleting workout:', error);
      toast.error('An error occurred while deleting.');
    } finally {
      closeConfirmationModal();
    }
  };

  if (isLoading) {
    return <p className="p-8 text-center text-gray-400">Loading workouts...</p>;
  }

  if (!workouts || workouts.length === 0) {
    return <p className="p-8 text-center text-gray-400">You haven't logged any workouts yet.</p>;
  }

  return (
    <>
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
                  onClick={() => onEditClick(workout)}
                  className="font-medium text-sky-500 hover:underline mr-4"
                >
                  Edit
                </button>
                <button
                  onClick={() => openConfirmationModal(workout)}
                  className="font-medium text-red-500 hover:underline"
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      
      <ConfirmDeleteModal 
        isOpen={isModalOpen}
        onClose={closeConfirmationModal}
        onConfirm={handleDelete}
        workout={workoutToDelete}
      />
      
    </>
  );
}

export default WorkoutList;