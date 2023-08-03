package com.hcc.controllers;

import com.hcc.entities.Assignment;
import com.hcc.repositories.AssignmentRepository;
import com.hcc.services.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/assignments")

public class AssignmentController {

    //@Autowired
    //UserService userService;
    @Autowired
    AssignmentService assignmentService;

    @GetMapping("/")
    public List<Assignment> getAssignmentByUser() {
        return assignmentService.getAssignmentsByUser();
    }

    @GetMapping("/{id}")
    public Assignment getAssignmentById(@PathVariable Long id){
        return assignmentService.getAssignmentById(id);
    }

    @PutMapping("/{id}")
    public Assignment updateAssignmentById(@PathVariable Long id, @RequestBody Assignment assignment) {
        return assignmentService.updateAssignmentById(id, assignment);
    }

    @PostMapping("/")
    public Assignment createAssignment(@RequestBody Assignment assignment) {
        return assignmentService.createAssignment(assignment);
    }


}
