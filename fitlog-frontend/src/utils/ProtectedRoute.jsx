import React, { useState, useEffect } from 'react';
import { Navigate } from 'react-router-dom';

function ProtectedRoute({ children }) {

    const [isAuthenticated, setIsAuthenticated] = useState(null); 

  useEffect(() => {

    const checkAuthStatus = async () => {
      try {
        const response = await fetch('http://localhost:8080/FitLogBackend/checkAuth', {
          credentials: 'include'
        });

        if (response.status === 200) {
          setIsAuthenticated(true); 
        } else {
          setIsAuthenticated(false); 
        }
      } catch (error) {
        console.error("Authentication check failed:", error);
        setIsAuthenticated(false); 
      }
    };

    checkAuthStatus();
  }, []); 


  if (isAuthenticated === null) {
    return <div>Loading...</div>; 
  }


  return isAuthenticated ? children : <Navigate to="/login" />;
}

export default ProtectedRoute;