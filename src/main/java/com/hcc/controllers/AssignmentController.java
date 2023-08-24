package com.hcc.controllers;

import com.hcc.dtos.ApiResponse;
import com.hcc.dtos.Assignments.AssignmentCreationRequestDto;
import com.hcc.dtos.Assignments.AssignmentUpdateRequestDto;
import com.hcc.entities.User;
import com.hcc.enums.AssignmentStatusEnum;
import com.hcc.enums.AuthorityEnum;
import com.hcc.services.AssignmentService;
import com.hcc.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {


    @Autowired
    UserService userService;
    @Autowired
    AssignmentService assignmentService;

    Map<AuthorityEnum, List<AssignmentStatusEnum>> validStatusChangesForRoleMap;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    /**
     * Retrieves assignments related to a given user.
     * If the user is a reviewer, it fetches assignments awaiting review.
     * If the user is a student, it fetches assignments submitted by the student.
     *
     * @param type Indicates whether the user is requesting assignments as a reviewer.
     * @param user The authenticated user making the request.
     * @return ApiResponse containing assignments related to the user.
     */
    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAssignmentsByUser(
            @RequestParam(name = "type", required = false) String type,
            @AuthenticationPrincipal User user) {
        ApiResponse response = assignmentService.getAssignmentsForUser(type, user);
        return ResponseEntity.ok(response);
    }


    /**
     * Fetches detailed information about an assignment based on its ID.
     *
     * @param id The ID of the assignment to be fetched.
     * @param user The authenticated user making the request.
     * @return ApiResponse containing details of the specified assignment.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAssignmentById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return assignmentService.getAssignmentDetailsById(id, user);
    }

    /**
     * Updates the details of an existing assignment based on its ID.
     *
     * @param id The ID of the assignment to be updated.
     * @param updateRequest Contains the details to be updated for the assignment.
     * @param user The authenticated user making the request.
     * @return ApiResponse containing the updated details of the assignment or an error message.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAssignmentById(
            @PathVariable Long id,
            @RequestBody AssignmentUpdateRequestDto updateRequest,
            @AuthenticationPrincipal User user) {
        return assignmentService.updateAssignmentDetails(id, updateRequest, user);
    }

    /**
     * Creates a new assignment for a user.
     *
     * @param requestDto Contains the details for the new assignment.
     * @param user The authenticated user making the request.
     * @return ApiResponse containing the details of the created assignment or an error message.
     */
    @PostMapping("/")
    public ResponseEntity<ApiResponse> createAssignment(
            @RequestBody AssignmentCreationRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        return assignmentService.createAssignmentForUser(requestDto, user);
    }


}
