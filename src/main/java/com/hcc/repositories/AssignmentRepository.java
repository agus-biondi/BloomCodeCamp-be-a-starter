package com.hcc.repositories;

import com.hcc.dtos.Assignments.AssignmentUpdateRequestDto;
import com.hcc.entities.Assignment;
import com.hcc.entities.User;
import com.hcc.enums.AssignmentEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findByUserAndNumber(User user, AssignmentEnum assignmentNumber);

    List<Assignment> findByUser(User user);

    List<Assignment> findByCodeReviewer(User user);

    List<Assignment> findByCodeReviewerOrCodeReviewerIsNull(User codeReviewer);


}