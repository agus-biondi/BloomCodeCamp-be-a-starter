import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import jwtDecode from 'jwt-decode';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBug, faFolderOpen, faSignOutAlt } from '@fortawesome/free-solid-svg-icons';


const LogoImage = styled.img`
  width: 175px;
  margin-bottom: 20px;
`;

const Page = styled.div`
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 20px;
`;

const Sidebar = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  height: 100%;
  width: 200px;
  background-color: #023D36;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px 0;
  box-shadow: 2px 0 5px rgba(0,0,0,0.2);
`;

const SidebarButton = styled.button`
  background-color: transparent;
  border: none;
  color: #FBCF75;
  font-size: 1.2em;
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  margin: 15px 0;
  transition: 0.3s;

  &:hover {
    color: #FBCF75;
    transform: scale(1.1);
  }
`;

const MainContent = styled.div`
  width: calc(100% - 200px);
  margin-left: 200px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: -100px;
`;

const TitleImage = styled.img`
  margin-left: 200px;
  width: auto;
  height: 210px;
  margin-bottom: 20px;
`;

const RoleCard = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 200px;
  height: 250px;
  margin: 10px;
  border-radius: 30px;
  background-color: #023D36;
  cursor: pointer;
  transition: transform 0.3s;
  &:hover {
    transform: scale(1.05);
  }
`;

const CardHeader = styled.div`
  width: 100%;
  height: 60%;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #3a3b3c;
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
`;

const CardSection = styled.div`
  width: 100%;
  height: 40%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #242526;
  border-bottom-left-radius: 5px;
  border-bottom-right-radius: 5px;
`;

const CardIcon = styled(FontAwesomeIcon)`
  color: #FBCF75;
  font-size: 3em;
`;

const CardText = styled.p`
  text-transform: capitalize;
  text-align: center;
  color: #FBCF75;
  font-weight: bold;
`;

const CardContainer = styled.div`

  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top:-100px;

`;

const HomePage = () => {
  const navigate = useNavigate();
  const [roles, setRoles] = useState([]);

  useEffect(() => {
    const token = localStorage.getItem('jwt');

    if (token) {
      const decodedToken = jwtDecode(token);
      const roles = decodedToken.scopes.map(scope => scope.authority);
      setRoles(roles);
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

      <TitleImage src="images/bloom_title_no_tagline.png" alt="Bloom Code Camp" />

      <Sidebar>
               <LogoImage src="images/bloom_icon_no_bg.png" alt="Bloom Logo" />
               <SidebarButton onClick={handleLogout}>
                 <FontAwesomeIcon icon={faSignOutAlt} />
                 Logout
               </SidebarButton>
           </Sidebar>

      <MainContent>
        <CardContainer>
          {roles.includes('ROLE_STUDENT') && (
            <RoleCard onClick={() => navigate('/student_dashboard')}>
              <CardHeader>
                <CardIcon icon={faFolderOpen} shake size="lg" />
              </CardHeader>
              <CardSection>
                <CardText>Student Dashboard</CardText>
              </CardSection>
            </RoleCard>
          )}
          {roles.includes('ROLE_REVIEWER') && (
            <RoleCard onClick={() => navigate('/reviewer_dashboard')}>
              <CardHeader>
                <CardIcon icon={faBug} />
              </CardHeader>
              <CardSection>
                <CardText>Review Students Assignments</CardText>
              </CardSection>
            </RoleCard>
          )}
        </CardContainer>
      </MainContent>
    </Page>
  );
}

export default HomePage;