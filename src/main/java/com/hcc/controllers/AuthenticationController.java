package com.hcc.controllers;

import com.hcc.dtos.AuthCredentialRequestDto;
import com.hcc.dtos.AuthCredentialResponseDto;
import com.hcc.entities.User;
import com.hcc.services.AuthenticationService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;


@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/auth/login")
    public ResponseEntity<AuthCredentialResponseDto> login(
            @RequestBody AuthCredentialRequestDto authCredentialRequest) {
        logger.info("login request");
        logger.info(authCredentialRequest.toString());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        return ResponseEntity.ok(authenticationService.login(authCredentialRequest));

    }

    @GetMapping("/auth/validate")
    public ResponseEntity<Boolean> validate(
            @RequestHeader("Authorization") String token, @AuthenticationPrincipal User user) {
        logger.info("validated token");
        return ResponseEntity.ok(true);
    }

    public AuthenticationController() {

    }

/*
    @ExceptionHandler(AccessDisabledException.class)
    public ResponseEntity<String> handleAccessDisabledException(AccessDisabledException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

 */

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleIncorrectCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }



}
