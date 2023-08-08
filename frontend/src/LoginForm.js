import React, { useState } from 'react';
import styled, { keyframes } from 'styled-components';
import axios from 'axios';

const rotate = keyframes`
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
`;

const Page = styled.div`
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(120deg, #0d1117, #161b22);
`;

const LoginForm = styled.div`
  width: 300px;
  padding: 20px;
  background: #242526;
  border-radius: 10px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
`;

const Input = styled.input`
  width: 90%;
  padding: 10px;
  margin: 10px 0;
  border: none;
  border-radius: 5px;
  background: #3a3b3c;
  color: white;
  outline: none;
  transition: transform 0.2s;
`;

const Button = styled.button`
  width: 97%;
  padding: 10px;
  margin: 10px 0;
  border: none;
  border-radius: 5px;
  background: #007a99;
  color: white;
  cursor: pointer;
  transition: background 0.3s;

  &:hover {
    background: #0099cc;
  }
`;

const LoadingIcon = styled.div`
  border: 2px solid #f3f3f3;
  border-top: 2px solid #3498db;
  border-radius: 50%;
  width: 12px;
  height: 12px;
  animation: ${rotate} 2s linear infinite;
  margin-left: 5px;
`;

const Header = styled.h1`
  color: #e4e6eb;
  font-family: 'Arial', sans-serif;
  text-align: center;
  margin-bottom: 20px;
`;

const ErrorMessage = styled.p`
  color: red;
  font-weight: bold;
`;

const LoginPage = () => {
  const [loading, setLoading] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const handleLogin = async () => {
    setLoading(true);
    setErrorMessage('');

    try {
          const response = await axios.post('http://localhost:8080/api/auth/login', {
            username: username,
            password: password
          });

          console.log(response.data.token);

        } catch (error) {
          if (error.response) {
            switch (error.response.status) {
              case 404:
                setErrorMessage('Username not found.');
                break;
              case 401:
                setErrorMessage('Incorrect username or password.');
                break;
              default:
                setErrorMessage('An error occurred. Please try again.');
                break;
            }
          } else {

            setErrorMessage('An error occurred, and I refuse to give more information');
          }
        } finally {
          setLoading(false);
        }
    };


  return (
      <Page>
        <LoginForm>
          <Header>Bloom Code Camp</Header>
            <Input type="text" placeholder="Username" onChange={e => setUsername(e.target.value)} value={username} />
            <Input type="password" placeholder="Password" onChange={e => setPassword(e.target.value)} value={password} />
            <Button onClick={handleLogin}>
            Login
            {loading && <LoadingIcon />}
          </Button>
          {errorMessage && <ErrorMessage>{errorMessage}</ErrorMessage>}
        </LoginForm>
      </Page>
    );
}

export default LoginPage;