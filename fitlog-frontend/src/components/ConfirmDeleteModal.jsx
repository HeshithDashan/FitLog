import React from 'react';

function ConfirmDeleteModal({ isOpen, onClose, onConfirm, workout }) {

    if (!isOpen) {
    return null;
  }

  return (

    <div className="fixed inset-0 bg-black bg-opacity-75 flex justify-center items-center z-50">
      
      <div className="bg-gray-800 rounded-lg shadow-xl p-6 w-full max-w-md mx-4">
        
        <h3 className="text-xl font-bold text-white mb-4">Confirm Deletion</h3>
        
        <p className="text-gray-300 mb-6">
          Are you sure you want to delete the workout: <br />
          <strong className="text-sky-400">{workout?.workoutType} on {new Date(workout?.logDate).toLocaleDateString()}</strong>?
          <br />
          This action cannot be undone.
        </p>
        
        <div className="flex justify-end gap-4">
          <button 
            onClick={onClose} 
            className="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-500 font-semibold"
          >
            Cancel
          </button>
          <button 
            onClick={onConfirm} 
            className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 font-semibold"
          >
            Confirm Delete
          </button>
        </div>

      </div>
    </div>
  );
}

export default ConfirmDeleteModal;