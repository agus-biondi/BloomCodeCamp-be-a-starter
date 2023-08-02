package com.hcc.repositories;

import com.hcc.entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository {
    List<Assignment> getAllAssignments();
    Assignment getAssignmentById(Long id);
    Assignment createAssignment(Assignment assignment);
    Assignment updateAssignment(Assignment assignment);
    void deleteAssignment(Long id);
}