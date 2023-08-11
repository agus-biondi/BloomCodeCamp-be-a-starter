import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';
import LoginPage from './LoginForm';
import HomePage from './HomePage';
import StudentDashboard from './StudentDashboard';
import axios from 'axios';
import { createGlobalStyle } from 'styled-components';

const GlobalStyle = createGlobalStyle`
  body {
    background: linear-gradient(120deg, #0d1117, #161b22);
    margin: 0;
    font-family: sans-serif;
  }
`;

function App() {

  return (
    <>
    <Router>
    <GlobalStyle />
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/homepage" element={<HomePage />} />
        <Route path="/reviewer_dashboard" element={<HomePage />} />
        <Route path="/student_dashboard" element={<StudentDashboard />} />
        <Route path="*" element={<LoginRedirect />} />
      </Routes>
    </Router>
    </>

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