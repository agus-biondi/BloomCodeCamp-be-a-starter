import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimesCircle, faSpinner, faCheckCircle } from '@fortawesome/free-solid-svg-icons';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const LogoImage = styled.img`
  width: 175px;
  position: absolute;
  top: 10px;
  left: 10px;
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
  width: 100%;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: -100px;
`;

const TitleImage = styled.img`
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

const PlaceholderText = styled.p`
  color: #FBCF75;
`;

const StudentDashboard = () => {
  const navigate = useNavigate();
  const [assignments, setAssignments] = useState({
          rejected: [],
          inReview: [],
          completed: []
      });


    useEffect(() => {
        axios.get('http://localhost:8080/api/assignments"')
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

  return (
    <Page>
      <LogoImage src="images/bloom_icon_no_bg.png" alt="Bloom Logo" />
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
                  <CardIcon icon={faSpinner} />
                  <CardText>{assignment.title}</CardText>
                </AssignmentCard>
              ))
            ) : (
              <PlaceholderText>No assignments completed at the moment.</PlaceholderText>
            )}
          </CardsContainer>
        </AssignmentSection>
      </MainContent>
      <LogoutButton onClick={handleLogout}>Logout</LogoutButton>
    </Page>
  );
}

export default StudentDashboard;
