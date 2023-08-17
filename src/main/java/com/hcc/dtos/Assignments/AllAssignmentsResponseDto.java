package com.hcc.dtos.Assignments;

import com.hcc.entities.Assignment;
import com.hcc.enums.AssignmentEnum;

import java.util.List;

public class AllAssignmentsResponseDto {

    private List<Assignment> assignments;
    private List<AssignmentEnum> unsubmittedAssignments;

    public AllAssignmentsResponseDto() {
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public List<AssignmentEnum> getUnsubmittedAssignments() {
        return unsubmittedAssignments;
    }

    public void setUnsubmittedAssignments(List<AssignmentEnum> unsubmittedAssignments) {
        this.unsubmittedAssignments = unsubmittedAssignments;
    }
}
