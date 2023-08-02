package com.hcc.controllers;

import com.hcc.entities.Assignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/assignments")

public class AssignmentController {

    //@Autowired
    //UserService userService;
    //@Autowired
    //AssignmentService assignmentService;

    @GetMapping("/")
    public List<Assignment> getAssignmentByUser() {
        return null;
    }

    @GetMapping("/{id}")
    public Assignment getAssignmentById(@PathVariable Long id){
        return null;
    }

    @PutMapping("/{id}")
    public Assignment updateAssignmentById(@PathVariable Long id, @RequestBody Assignment assignment) {
        return null;
    }

    @PostMapping("/")
    public Assignment createAssignment(@RequestBody Assignment assignment) {
        return null;
    }


}
