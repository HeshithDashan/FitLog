import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom'; 
import toast from 'react-hot-toast';

function LoginPage() {

    const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate(); 

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new URLSearchParams();
    formData.append('email', email);
    formData.append('password', password);

    try {
      const response = await fetch('http://localhost:8080/FitLogBackend/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: formData.toString(),
        credentials: 'include', 
      });

      if (response.ok) {
        toast.success('Login Successful!');
        navigate('/');
      } else {
        toast.error('Invalid email or password. Please try again.');
      }
    } catch (error) {
      toast.error('An error occurred. Please try again later.');
      console.error('Login error:', error);
    }
  };

  return (
    <div className="bg-gray-900 min-h-screen flex items-center justify-center">
      <div className="w-full max-w-md bg-gray-800 p-8 rounded-lg shadow-lg">
        <h1 className="text-4xl font-bold text-center mb-8 text-sky-400">
          Welcome Back!
        </h1>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-300 mb-2" htmlFor="email">
              Email Address
            </label>
            <input
              className="w-full p-3 bg-gray-700 rounded text-white border border-gray-600 focus:outline-none focus:ring-2 focus:ring-sky-500"
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="mb-6">
            <label className="block text-gray-300 mb-2" htmlFor="password">
              Password
            </label>
            <input
              className="w-full p-3 bg-gray-700 rounded text-white border border-gray-600 focus:outline-none focus:ring-2 focus:ring-sky-500"
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <button
            type="submit"
            className="w-full bg-sky-600 hover:bg-sky-700 text-white font-bold py-3 px-4 rounded focus:outline-none focus:shadow-outline"
          >
            Login
          </button>
        </form>
        <p className="text-center text-gray-400 mt-6">
          Don't have an account?{' '}
          <Link to="/register" className="text-sky-400 hover:underline">
            Sign Up
          </Link>
        </p>
      </div>
    </div>
  );
}


export default LoginPage;