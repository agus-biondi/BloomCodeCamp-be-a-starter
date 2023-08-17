import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
    faHome,
    faSignOutAlt,
    faSpinner,
    faCheckCircle,
    faClipboardList
} from '@fortawesome/free-solid-svg-icons';
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
  padding: 10px 10px;
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


const ImportantSidebarButton = styled(SidebarButton)`
  background-color: transparent;
  border: 2px solid #FBCF75;
  padding: 5px 10px;
  box-shadow: 0px 0px 8px 2px rgba(255,167,38,0.6);
  border-radius: 16px;
  &:hover {
    transform: scale(1.05);
    box-shadow: 0px 0px 12px 4px rgba(255,167,38,0.7);
    border-radius: 8px;
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
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #3a3b3c;
`;

const CardIconWrapper = styled.div`
  flex-grow: 2;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #023D36;
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

const ModalContent = styled.div`
  width: 80%;
  max-width: 500px;
  padding: 20px;
  background-color: #3a3b3c;
  border: 1px solid #023D36;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 1001;
  color: #023D36;
`;

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

const ModalStyledForm = styled.form`
    display: flex;
    flex-direction: column;
    gap: 10px;
`;

const ModalStyledLabel = styled.label`
    color: #FBCF75;
`;

const ModalStyledInput = styled.input`
    padding: 10px;
    border: 1px solid #023D36;
    border-radius: 5px;
    &:focus {
        outline: none;
        border-color: #FBCF75;
    }
`;

const ModalStyledSelect = styled.select`
    padding: 10px;
    font-size: 1em;
    border: 1px solid #023D36;
    border-radius: 5px;
    &:focus {
        outline: none;
        border-color: #FBCF75;
    }
`;

const ModalStyledButton = styled.button`
    padding: 10px 20px;
    background-color: #023D36;
    color: #FBCF75;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: 0.3s;
    &:hover {
        background-color: #FBCF75;
        color: #023D36;
    }
`;

const UserDetails = styled.div`
  //background-color: #3a3b3c;
  //color: #FBCF75;
    background-color: #FBCF75;
    border: none;
    font-size: 0.8em;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 15px 0;
    padding: 5px 6px;
    border-radius: 20px;
    align-self: flex-start;
  `;

const AssignmentDisplaySection = ({ header, assignmentsList, icon }) => (
  <AssignmentSection>
    <SectionHeader>{header}</SectionHeader>
    <CardsContainer>
      {assignmentsList.length > 0 ? (
        assignmentsList.map((assignment, idx) => (
          <AssignmentCard key={idx}>
          <UserDetails>{assignment.user.username}</UserDetails>
              <CardIconWrapper>
                <CardIcon icon={icon} />
              </CardIconWrapper>

              <AssignmentNumberSection>
                  {assignment.title || assignment.number}
              </AssignmentNumberSection>

              <SidebarButton>
                  Review
              </SidebarButton>
          </AssignmentCard>
        ))
      ) : (
        <PlaceholderText>No {header.toLowerCase()} at the moment.</PlaceholderText>
      )}
    </CardsContainer>
  </AssignmentSection>
);


const AssignmentNumberSection = styled.div`
  flex: 1;
  background-color: #242526;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px;
    text-transform: capitalize;
    text-align: center;
    color: #FBCF75;
    font-weight: bold;
`;

const ReviewerDashboard = () => {
    const token = localStorage.getItem("jwt");
    const navigate = useNavigate();
    const [assignments, setAssignments] = useState({
        inReview: [],
        submittedResubmitted: [],
        completed: []
    });

    useEffect(() => {
        axios.get('http://localhost:8080/api/assignments/', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    },
                    params: {
                        type: 'reviewer'
                    }
                })
            .then(response => {
                if (response.data.success) {
                    const fetchedAssignments = response.data.data.assignments;
                    console.log(fetchedAssignments);
                    setAssignments({
                        inReview: fetchedAssignments.filter(assignment =>
                            ['CLAIMED', 'RECLAIMED'].includes(assignment.status)
                        ),
                        submittedResubmitted: fetchedAssignments.filter(assignment =>
                            ['SUBMITTED', 'RESUBMITTED'].includes(assignment.status)
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
                <SidebarButton onClick={handleLogout}>
                    <FontAwesomeIcon icon={faSignOutAlt} />
                    Logout
                </SidebarButton>
            </Sidebar>

            <TitleImage src="images/bloom_title_no_tagline.png" alt="Bloom Code Camp" />
            <MainContent>

                <AssignmentDisplaySection
                    header="In Review"
                    assignmentsList={assignments.inReview}
                    icon={faSpinner}
                />

                <AssignmentDisplaySection
                    header="Submitted for Review and Resubmitted"
                    assignmentsList={assignments.submittedResubmitted}
                    icon={faClipboardList}
                />

                <AssignmentDisplaySection
                    header="Completed"
                    assignmentsList={assignments.completed}
                    icon={faCheckCircle}
                />

            </MainContent>
        </Page>
    );
}

export default ReviewerDashboard;
