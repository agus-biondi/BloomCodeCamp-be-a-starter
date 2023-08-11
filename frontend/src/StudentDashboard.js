import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus, faHome, faSignOutAlt, faTimesCircle, faSpinner, faCheckCircle } from '@fortawesome/free-solid-svg-icons';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

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
  padding: 10px 0;  // Adjusted padding based on the logo's size
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

const MainContent = styled.div`
  width: calc(100% - 200px);  // This subtracts the sidebar width
  margin-left: 200px;        // This pushes the main content to the right of the sidebar
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

const AssignmentSection = styled.div`
  width: 80%;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 10px 0;
`;

const SectionHeader = styled.h1`
  color: #FBCF75;
  margin-bottom: 10px;
`;

const CardsContainer = styled.div`
  display: flex;
  gap: 20px;
  overflow-x: auto;
  overflow: hidden;
`;

const AssignmentCard = styled.div`
  width: 200px;
  height: 150px;
  background-color: #023D36;
  display: flex;
  flex-direction: column;
  border-radius: 15px;
  overflow: hidden;
  transition: transform 0.3s;
  cursor: pointer;
`;

const CardHeader = styled.div`
  flex: 2;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #3a3b3c;
  &:hover {
    background-color: #343536;
  }
`;

const CardFooter = styled.div`
  flex: 1;
  background-color: #242526;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const CardIcon = styled(FontAwesomeIcon)`
  color: #FBCF75;
  font-size: 3em;
`;

const CardText = styled.p`
  text-transform: capitalize;
  text-align: center;
  color: #FBCF75;
`;

const LogoutButton = styled.button`
  width: 150px;
  height: 50px;
  background-color: #023D36;
  color: #FBCF75;
  border: none;
  border-radius: 5px;
  cursor: pointer;

  &:hover {
    background-color: #FBCF75;
    color: #023D36;
  }
`;

const HomeButton = styled.button`
  width: 150px;
  height: 50px;
  background-color: #023D36;
  color: #FBCF75;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-right: 10px;

  &:hover {
    background-color: #FBCF75;
    color: #023D36;
  }
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
`;

const PlaceholderText = styled.p`
  color: #FBCF75;
`;

const CreateAssignmentCard = styled(AssignmentCard)`
  background-color: #3a3b3c;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const PlusIcon = styled(FontAwesomeIcon)`
  color: #FBCF75;
  font-size: 3em;
`;

const handleCreateAssignment = () => {

};

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
`;

const ModalContent = styled.div`
  width: 80%;
  max-width: 500px;
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 1001;
`;

const StudentDashboard = () => {
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [assignments, setAssignments] = useState({
          rejected: [],
          inReview: [],
          completed: []
      });


    useEffect(() => {
        axios.get('http://localhost:8080/api/assignments')
            .then(response => {
                if (response.data.success) {
                    const fetchedAssignments = response.data.data;

                    setAssignments({
                        rejected: fetchedAssignments.filter(assignment => assignment.status === 'REJECTED'),
                        inReview: fetchedAssignments.filter(assignment =>
                            ['SUBMITTED', 'CLAIMED', 'RESUBMITTED', 'RECLAIMED'].includes(assignment.status)
                        ),
                        completed: fetchedAssignments.filter(assignment => assignment.status === 'COMPLETED')
                    });
                }
            })
            .catch(error => {
                console.error("Error fetching assignments:", error);
            });
    }, []);


    const handleLogout = () => {
        localStorage.removeItem('jwt');
        navigate('/login');
    };

    const handleHomeClick = () => {
        navigate('/homepage');
    };

  return (
    <Page>
     <Sidebar>
         <LogoImage src="images/bloom_icon_no_bg.png" alt="Bloom Logo" />

         <SidebarButton onClick={handleHomeClick}>
           <FontAwesomeIcon icon={faHome} />
           Home
         </SidebarButton>
         <SidebarButton onClick={() => setIsModalOpen(true)}>
           <FontAwesomeIcon icon={faPlus} />
           New Assignment
         </SidebarButton>
         <SidebarButton onClick={handleLogout}>
           <FontAwesomeIcon icon={faSignOutAlt} />
           Logout
         </SidebarButton>
     </Sidebar>

      {isModalOpen && (
        <ModalOverlay onClick={() => setIsModalOpen(false)}>
          <ModalContent onClick={(e) => e.stopPropagation()}>
            {/* Modal content goes here */}
          </ModalContent>
        </ModalOverlay>
      )}
      <TitleImage src="images/bloom_title_no_tagline.png" alt="Bloom Code Camp" />
      <MainContent>

        <AssignmentSection>
          <SectionHeader>Rejected Assignments</SectionHeader>
              <CardsContainer>
                {assignments.rejected.length > 0 ? (
                  assignments.rejected.map((assignment, idx) => (
                    <AssignmentCard key={idx}>
                      <CardIcon icon={faTimesCircle} />
                      <CardText>{assignment.title}</CardText>
                    </AssignmentCard>
                  ))
                ) : (
                  <PlaceholderText>No rejected assignments at the moment.</PlaceholderText>
                )}
              </CardsContainer>
        </AssignmentSection>
        <AssignmentSection>
          <SectionHeader>Assignments In Review</SectionHeader>
            <CardsContainer>
              {assignments.inReview.length > 0 ? (
                assignments.inReview.map((assignment, idx) => (
                  <AssignmentCard key={idx}>
                    <CardIcon icon={faSpinner} />
                    <CardText>{assignment.title}</CardText>
                  </AssignmentCard>
                ))
              ) : (
                <PlaceholderText>No assignments in review at the moment.</PlaceholderText>
              )}
            </CardsContainer>
        </AssignmentSection>
        <AssignmentSection>
          <SectionHeader>Completed Assignments</SectionHeader>
          <CardsContainer>
            {assignments.completed.length > 0 ? (
              assignments.completed.map((assignment, idx) => (
                <AssignmentCard key={idx}>
                  <CardIcon icon={faCheckCircle} />
                  <CardText>{assignment.title}</CardText>
                </AssignmentCard>
              ))
            ) : (
              <PlaceholderText>No assignments completed at the moment.</PlaceholderText>
            )}
          </CardsContainer>
        </AssignmentSection>
      </MainContent>
    </Page>
  );
}

export default StudentDashboard;
