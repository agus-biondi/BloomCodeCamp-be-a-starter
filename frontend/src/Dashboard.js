import React from 'react';
import styled from 'styled-components';

// Styled-components
const DashboardContainer = styled.div`
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #f7f7f7;
`;

const Title = styled.h1`
  font-size: 2em;
  color: #333;
  margin-bottom: 20px;
`;

const Content = styled.div`
  max-width: 800px;
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
`;

const Dashboard = () => {
  return (
    <DashboardContainer>
      <Title>Welcome to the Authenticated User Dashboard</Title>
      <Content>
        <p>
          DASHBOARD!!
          </p>
      </Content>
    </DashboardContainer>
  );
}

export default Dashboard;