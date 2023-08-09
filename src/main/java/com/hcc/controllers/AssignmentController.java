package com.hcc.controllers;

import com.hcc.dtos.ApiResponse;
import com.hcc.dtos.Assignments.AssignmentCreationRequestDto;
import com.hcc.dtos.Assignments.AssignmentResponseDto;
import com.hcc.dtos.Assignments.AssignmentUpdateRequestDto;
import com.hcc.entities.Assignment;
import com.hcc.entities.User;
import com.hcc.enums.AssignmentEnum;
import com.hcc.enums.AssignmentStatusEnum;
import com.hcc.enums.AuthorityEnum;
import com.hcc.services.AssignmentService;
import com.hcc.services.UserService;
import com.hcc.utils.AssignmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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


    @GetMapping("/")
    public List<Assignment> getAssignmentByUser(@AuthenticationPrincipal User user) {
        return assignmentService.getAssignmentsByUser(user);
    }

    @GetMapping("/{id}")
    public Assignment getAssignmentById(@PathVariable Long id){
        return assignmentService.getAssignmentById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAssignmentById(
            @PathVariable Long id,
            @RequestBody AssignmentUpdateRequestDto updateRequest,
            @AuthenticationPrincipal User user) {

        ApiResponse response = new ApiResponse();



        //if (updateRequest.getStatus() )

        return null;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> createAssignment(
            @RequestBody AssignmentCreationRequestDto requestDto,
            @AuthenticationPrincipal User user) {

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

        assignment.setStatus(AssignmentStatusEnum.NOT_SUBMITTED);

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
