package com.hcc.services;

import com.hcc.entities.Assignment;
import com.hcc.exceptions.ResourceNotFoundException;
import com.hcc.repositories.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    public List<Assignment> getAssignmentsByUser() {
        return null;
    }
    
    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found: " + id)
                );
    }

    public Assignment updateAssignmentById(Long id, Assignment assignment) {
        return null;
    }

    public Assignment createAssignment(Assignment assignment) {
        return null;
    }
}
