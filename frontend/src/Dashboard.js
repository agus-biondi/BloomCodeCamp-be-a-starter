import React, {
	useState, useEffect
} from 'react';
import styled from 'styled-components';
import axios from 'axios';
import {
	useNavigate
} from 'react-router-dom';

const LogoImage = styled.img`
  width: 150px;
  top: 10px;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 20px;
`;

const Page = styled.div`
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  background: linear-gradient(120deg, #0d1117, #161b22);
`;

const Title = styled.h1`
  font-size: 2em;
  color: #e4e6eb;
  margin-bottom: 20px;
`;

const Content = styled.div`
  width: 300px;
  background-color: #242526;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
  text-align: center;
`;

const LogoutButton = styled.button`
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
  &:hover {
    background: #0099cc;
  }
`;

const Dashboard = () => {
    const navigate = useNavigate();
	const [loading, setLoading] = useState(false);

useEffect(() => {
    const token = localStorage.getItem('jwt');

    if (token) {
        setLoading(true);
        axios.get('http://localhost:8080/api/auth/validate', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => {
            if (response.data) {
                setLoading(false);
            }
        })
        .catch(err => {
            localStorage.removeItem('jwt');
            navigate('/login');
        })
        .finally(() => {
            setLoading(false);
        });
    } else {
        navigate('/login');
    }
    }, [navigate]);

    const handleLogout = () => {
        localStorage.removeItem('jwt');
        navigate('/login');
    };

  return (
    <Page>
      <LogoImage src="/images/bloom_icon_no_bg.png" alt="Bloom Logo" />
      <Title>Welcome to the Authenticated User Dashboard</Title>
      <Content>
        <p>
          DASHBOARD!!
        </p>
        <LogoutButton onClick={handleLogout}>
          Logout
        </LogoutButton>
      </Content>
    </Page>
  );
}
export default Dashboard;