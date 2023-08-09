import React from 'react';
import styled, { keyframes } from 'styled-components';


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
  visibility: hidden;
`;

const Button = styled.button`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 97%;
  padding: 10px;
  margin: 10px 0;
  border: none;
  border-radius: 5px;
  background: #007a99;
  color: white;
  cursor: pointer;
  transition: background 0.3s;
  visibility: hidden;
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
  visibility: hidden;
`;

const LoadingComponent = () => (
      <Page>
        <LoginForm>
          <Header>Bloom Code Camp</Header>
            <Input type="text" placeholder="Username" />
            <Input type="password" placeholder="Password" />
            <Button>
               Login
            </Button>

        </LoginForm>
      </Page>
);

export default LoadingComponent;