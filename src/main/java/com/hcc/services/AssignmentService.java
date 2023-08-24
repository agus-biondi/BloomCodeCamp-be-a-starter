package com.hcc.services;

import com.hcc.controllers.AuthenticationController;
import com.hcc.dtos.ApiResponse;
import com.hcc.dtos.Assignments.AllAssignmentsResponseDto;
import com.hcc.dtos.Assignments.AssignmentCreationRequestDto;
import com.hcc.dtos.Assignments.AssignmentResponseDto;
import com.hcc.dtos.Assignments.AssignmentUpdateRequestDto;
import com.hcc.entities.Assignment;
import com.hcc.entities.User;
import com.hcc.enums.AssignmentEnum;
import com.hcc.enums.AssignmentStatusEnum;
import com.hcc.enums.AuthorityEnum;
import com.hcc.exceptions.ResourceNotFoundException;
import com.hcc.repositories.AssignmentRepository;
import com.hcc.utils.AssignmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentMapper assignmentMapper;

    Map<AuthorityEnum, Set<AssignmentStatusEnum>> validStatusChangesForRoleMap;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    /**
     * Retrieves assignments related to a given user.
     * If the user is a reviewer, it fetches assignments awaiting review.
     * If the user is a student, it fetches assignments submitted by the student.
     *
     * @param type Indicates whether the user is requesting assignments as a reviewer. "reviewer" or null
     * @param user The authenticated user making the request.
     * @return ApiResponse containing the assignments related to the user.
     */
    public ApiResponse getAssignmentsForUser(String type, User user) {
        ApiResponse response = new ApiResponse();
        AllAssignmentsResponseDto dto = new AllAssignmentsResponseDto();
        List<Assignment> assignments;
        List<AssignmentEnum> unsubmittedAssignments = null;

        logger.info("get assignment by user");
        boolean isReviewer = type != null && type.equals("reviewer") &&
                user.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals(AuthorityEnum.ROLE_REVIEWER.name()));

        if (isReviewer) {
            assignments = getAssignmentByCodeReviewerOrNoReviewer(user);
            logger.info("this is a reviewer");
        } else {
            assignments = getAssignmentsByUser(user);

            unsubmittedAssignments = Arrays.asList(AssignmentEnum.values())
                    .stream()
                    .filter(ua -> !assignments.stream()
                            .anyMatch(a -> a.getNumber().equals(ua)))
                    .collect(Collectors.toList());
        }

        response.setSuccess(true);
        dto.setAssignments(assignments);
        dto.setUnsubmittedAssignments(unsubmittedAssignments);
        response.setData(dto);
        return response;
    }

    /**
     * Fetches detailed information about an assignment based on its ID.
     *
     * @param id The ID of the assignment to be fetched.
     * @param user The authenticated user making the request.
     * @return ApiResponse containing details of the specified assignment.
     */
    public ResponseEntity<ApiResponse> getAssignmentDetailsById(Long id, User user) {
        ApiResponse response = new ApiResponse();
        Assignment assignment;
        try {
            assignment = getAssignmentById(id);

            if (!assignment.getUser().getUsername().equals(user.getUsername()) &&
                    !user.getAuthorities().contains(AuthorityEnum.ROLE_REVIEWER.name())) {
                response.setSuccess(false);
                response.setMessage("You don't have permission to see this");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.setSuccess(false);
            response.setMessage("Assignment not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Could not complete your request");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    /**
     * Updates the details of an existing assignment based on its ID.
     *
     * @param id The ID of the assignment to be updated.
     * @param updateRequest Contains the details to be updated for the assignment.
     * @param user The authenticated user making the request.
     * @return ApiResponse containing the updated details of the assignment or an error message.
     */
    public ResponseEntity<ApiResponse> updateAssignmentDetails(Long id, AssignmentUpdateRequestDto updateRequest, User user) {
        ApiResponse response = new ApiResponse();

        //User doesn't have authority to act the way he's claiming
        boolean hasRequiredAuthority = user.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority()
                        .equals(updateRequest.getUpdateAssignmentAsRole().name())
                );

        if (!hasRequiredAuthority) {
            response.setSuccess(false);
            response.setMessage(String.format("Can't update assignment with a role of %s because user %s doesn't have that role",
                    updateRequest.getUpdateAssignmentAsRole(),
                    user.getUsername()));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        //Modification type is not allowed for user role, e.g. Mark as Completed as a Student
        if (!isValidStatusChange(updateRequest.getStatus(), updateRequest.getUpdateAssignmentAsRole())) {
            response.setSuccess(false);
            response.setMessage(String.format("I'm afraid I can't let you do that Dave...Change an assignment to %s with the role of %s, that is.",
                    updateRequest.getStatus(),
                    user.getAuthorities()));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        //Assignment exists?
        Assignment existingAssignment;
        try {
            existingAssignment = getAssignmentById(id);
        } catch (ResourceNotFoundException e) {
            response.setSuccess(false);
            response.setMessage("Assignment not found " + id);
            return ResponseEntity.badRequest().body(response);
        }

        //Student can only change his own assignment
        if (updateRequest.getUpdateAssignmentAsRole().equals(AuthorityEnum.ROLE_STUDENT) &&
                !existingAssignment.getUser().getUsername().equals(user.getUsername())) {
            response.setSuccess(false);
            response.setMessage("This isn't yours");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        if (updateRequest.getUpdateAssignmentAsRole().equals(AuthorityEnum.ROLE_REVIEWER)) {
            updateRequest.setCodeReviewer(user);
        }

        try {
            Assignment updatedAssignment = updateAssignment(existingAssignment, updateRequest);
            AssignmentResponseDto responseDto = assignmentMapper.toResponseDto(updatedAssignment);
            response.setSuccess(true);
            response.setData(responseDto);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error updating assignment.");

        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Creates a new assignment for a user.
     *
     * @param requestDto Contains the details for the new assignment.
     * @param user The authenticated user making the request.
     * @return ApiResponse containing the details of the created assignment or an error message.
     */
    public ResponseEntity<ApiResponse> createAssignmentForUser(AssignmentCreationRequestDto requestDto, User user) {
        ApiResponse response = new ApiResponse();
        logger.info(requestDto.toString());


        if (requestDto.getBranch().isEmpty() || requestDto.getBranch().isBlank()) {
            response.setSuccess(false);
            response.setMessage("Invalid Branch");
            return ResponseEntity.badRequest().body(response);
        }

        if (requestDto.getGithubUrl().isEmpty() ||
                requestDto.getBranch().isBlank() ||
                requestDto.getGithubUrl().isEmpty() ||
                requestDto.getGithubUrl().isBlank()) {
            response.setSuccess(false);
            response.setMessage("Complete both Github Url and Branch");
            return ResponseEntity.badRequest().body(response);
        }

        if (!AssignmentEnum.isValidName(requestDto.getNumber())) {
            response.setSuccess(false);
            response.setMessage("Invalid Assignment Number");
            return ResponseEntity.badRequest().body(response);
        }

        Assignment assignment = assignmentMapper.creationDtoToEntity(requestDto, user);

        boolean userHasStudentRole = user.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT"));
        if (!userHasStudentRole) {
            response.setSuccess(false);
            response.setMessage("User is not a student");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        if (doesUserHaveAssignment(user, assignment.getNumber())) {
            response.setSuccess(false);
            response.setMessage("Assignment already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        assignment.setStatus(AssignmentStatusEnum.SUBMITTED);

        try {
            Assignment savedAssignment = createAssignment(assignment);
            AssignmentResponseDto responseDto = assignmentMapper.toResponseDto(savedAssignment);
            response.setSuccess(true);
            response.setData(responseDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("I can't give you a more detailed error message because I am a teapot. Try again once I cease to be a teapot.");
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(response);
        }
    }


    public List<Assignment> getAssignmentsByUser(User user) {
        return assignmentRepository.findByUser(user);
    }
    public List<Assignment> getAssignmentsByCodeReviewer(User reviewer) {
        return assignmentRepository.findByCodeReviewer(reviewer);
    }

    public List<Assignment> getAssignmentByCodeReviewerOrNoReviewer(User reviewer) {
        return assignmentRepository.findByCodeReviewerOrCodeReviewerIsNull(reviewer);
    }

    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }


    /**
     * Checks if a user already has submitted a specified assignment.
     *
     * @param user             The user.
     * @param assignmentNumber The assignment number.
     * @return True if the user has submitted the assignment, otherwise false.
     */
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


    /**
     * Checks if a status change for an assignment is valid based on the authority.
     *
     * @param status    The new status.
     * @param authority The authority making the change.
     * @return True if the change is valid, otherwise false.
     */
    private boolean isValidStatusChange(AssignmentStatusEnum status, AuthorityEnum authority) {

        List<AssignmentStatusEnum> validStatuses = validStatusChangesForRoleMap.get(authority);
        if (validStatuses != null && validStatuses.contains(status)) {
            return true;
        }
        return false;
    }

    /**
     * Initializes valid status changes for different roles upon bean creation.
     */
    @PostConstruct
    public void init() {

        this.validStatusChangesForRoleMap = new HashMap<>();

        validStatusChangesForRoleMap.put(AuthorityEnum.ROLE_STUDENT, Set.of(
                AssignmentStatusEnum.SUBMITTED,
                AssignmentStatusEnum.RESUBMITTED
        ));

        validStatusChangesForRoleMap.put(AuthorityEnum.ROLE_REVIEWER, Set.of(
                AssignmentStatusEnum.CLAIMED,
                AssignmentStatusEnum.REJECTED,
                AssignmentStatusEnum.RECLAIMED,
                AssignmentStatusEnum.COMPLETED
        ));

    }

}
