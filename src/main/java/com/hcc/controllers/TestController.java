package com.hcc.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
public class TestController {


    @GetMapping
    ResponseEntity<?> welcome() {
        return ResponseEntity.ok("Hello");
    }
    /*
    @GetMapping("/login2")
    public String getLoginPage() {
        return "login2";  // Name of the Thymeleaf template
    }*/
}
