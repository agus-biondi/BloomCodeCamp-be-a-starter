import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';
import LoginPage from './LoginForm';
import Dashboard from './Dashboard';
import LoadingComponent from './LoadingComponent'
import axios from 'axios';


function App() {
   const [isAuthenticated, setIsAuthenticated] = useState(false);
   const [isLoading, setIsLoading] = useState(true);

   useEffect(() => {
      const token = localStorage.getItem('jwt');

      if (token) {
         axios.get('http://localhost:8080/api/auth/validate', {
             headers: {
                 'Authorization': `Bearer ${token}`
             }
         })
            .then(response => {
               if (response.data) {
                  setIsAuthenticated(true);
               } else {
                  localStorage.removeItem('jwt');
               }
            })
            .catch(err => {
               console.error("Error validating token:", err);
               localStorage.removeItem('jwt');
            })
            .finally(() => {
                setIsLoading(false);
            });
      } else {
        setIsLoading(false);
      }
   }, []);

  const handleLogout = () => {
    localStorage.removeItem('jwt');
    setIsAuthenticated(false);
  };

  if (isLoading) {
    return <LoadingComponent />
  }


  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/dashboard" element={isAuthenticated ? <Dashboard handleLogout={handleLogout} /> : <LoginRedirect />} />
        <Route path="/loading" element={<LoadingComponent />} />
        <Route path="*" element={<LoginRedirect />} />
      </Routes>
    </Router>
  );
}

function LoginRedirect() {
  const navigate = useNavigate();
  useEffect(() => {
    navigate("/login");
  }, []);
  return null;
}

export default App;