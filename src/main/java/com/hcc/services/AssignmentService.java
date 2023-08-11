package com.hcc.services;

import com.hcc.dtos.Assignments.AssignmentUpdateRequestDto;
import com.hcc.entities.Assignment;
import com.hcc.entities.User;
import com.hcc.enums.AssignmentEnum;
import com.hcc.enums.AuthorityEnum;
import com.hcc.exceptions.ResourceNotFoundException;
import com.hcc.repositories.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    public List<Assignment> getAssignmentsByUser(User user) {
        return assignmentRepository.findByUser(user);
    }
    public List<Assignment> getAssignmentsByCodeReviewer(User reviewer) {
        return assignmentRepository.findByCodeReviewer(reviewer);
    }

    public boolean doesUserHaveAssignment(User user, AssignmentEnum assignmentNumber) {
        return assignmentRepository.findByUserAndNumber(user, assignmentNumber).isPresent();
    }
    
    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("")
                );
    }

    public Assignment updateAssignment(Assignment existingAssignment, AssignmentUpdateRequestDto updateRequest) {
        if(updateRequest.getUpdateAssignmentAsRole().equals(AuthorityEnum.ROLE_STUDENT)) {

            if(updateRequest.getGithubUrl() != null) {
                existingAssignment.setGithubUrl(updateRequest.getGithubUrl());
            }

            if(updateRequest.getBranch() != null) {
                existingAssignment.setBranch(updateRequest.getBranch());
            }

            if(updateRequest.getStatus() != null) {
                existingAssignment.setStatus(updateRequest.getStatus());
            }

        } else if(updateRequest.getUpdateAssignmentAsRole().equals(AuthorityEnum.ROLE_REVIEWER)) {

            if(updateRequest.getReviewVideoUrl() != null) {
                existingAssignment.setReviewVideoUrl(updateRequest.getReviewVideoUrl());
            }

            if(updateRequest.getStatus() != null) {
                existingAssignment.setStatus(updateRequest.getStatus());
            }
        }
        return assignmentRepository.save(existingAssignment);
    }

    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }


}
