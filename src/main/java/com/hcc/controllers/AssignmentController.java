package com.hcc.controllers;

import com.hcc.dtos.ApiResponse;
import com.hcc.dtos.Assignments.AssignmentCreationRequestDto;
import com.hcc.dtos.Assignments.AssignmentResponseDto;
import com.hcc.dtos.Assignments.AssignmentUpdateRequestDto;
import com.hcc.dtos.Assignments.GetAssignmentsRequestDto;
import com.hcc.entities.Assignment;
import com.hcc.entities.Authority;
import com.hcc.entities.User;
import com.hcc.enums.AssignmentEnum;
import com.hcc.enums.AssignmentStatusEnum;
import com.hcc.enums.AuthorityEnum;
import com.hcc.exceptions.ResourceNotFoundException;
import com.hcc.services.AssignmentService;
import com.hcc.services.UserService;
import com.hcc.utils.AssignmentMapper;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;


@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentMapper assignmentMapper;

    @Autowired
    UserService userService;
    @Autowired
    AssignmentService assignmentService;

    Map<AuthorityEnum, List<AssignmentStatusEnum>> validStatusChangesForRoleMap;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAssignmentsByUser(
            @RequestParam(name = "type", required = false) String type,
            @AuthenticationPrincipal User user) {
        ApiResponse response = new ApiResponse();
        List<Assignment> assignments;

        logger.info("get assignment by user");
        boolean isReviewer = type != null && type.equals("reviewer") &&
            user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(AuthorityEnum.ROLE_REVIEWER.name()));

        if (isReviewer) {
            assignments = assignmentService.getAssignmentsByCodeReviewer(user);
        } else {
            assignments = assignmentService.getAssignmentsByUser(user);
        }

        response.setSuccess(true);
        response.setData(assignments);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAssignmentById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user){

        ApiResponse response = new ApiResponse();
        Assignment assignment;
        try {
            assignment = assignmentService.getAssignmentById(id);

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

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAssignmentById(
            @PathVariable Long id,
            @RequestBody AssignmentUpdateRequestDto updateRequest,
            @AuthenticationPrincipal User user) {

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
            existingAssignment = assignmentService.getAssignmentById(id);
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

        try {
            Assignment updatedAssignment = assignmentService.updateAssignment(existingAssignment, updateRequest);
            AssignmentResponseDto responseDto = assignmentMapper.toResponseDto(updatedAssignment);

            response.setSuccess(true);
            response.setData(responseDto);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error updating assignment.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> createAssignment(
            @RequestBody AssignmentCreationRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        logger.info(requestDto.toString());
        ApiResponse response = new ApiResponse();

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

        if (assignmentService.doesUserHaveAssignment(user, assignment.getNumber())) {
            response.setSuccess(false);
            response.setMessage("Assignment already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        assignment.setStatus(AssignmentStatusEnum.SUBMITTED);

        try {
            Assignment savedAssignment = assignmentService.createAssignment(assignment);
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

    private boolean isValidStatusChange(AssignmentStatusEnum status, AuthorityEnum authority) {

        List<AssignmentStatusEnum> validStatuses = validStatusChangesForRoleMap.get(authority);
        if (validStatuses != null && validStatuses.contains(status)) {
            return true;
        }
        return false;
    }
    @PostConstruct
    public void init() {

        this.validStatusChangesForRoleMap = new HashMap<>();

        validStatusChangesForRoleMap.put(AuthorityEnum.ROLE_STUDENT, Arrays.asList(
                AssignmentStatusEnum.SUBMITTED,
                AssignmentStatusEnum.RESUBMITTED
        ));

        validStatusChangesForRoleMap.put(AuthorityEnum.ROLE_REVIEWER, Arrays.asList(
                AssignmentStatusEnum.CLAIMED,
                AssignmentStatusEnum.REJECTED,
                AssignmentStatusEnum.RECLAIMED,
                AssignmentStatusEnum.COMPLETED
        ));

    }

}
