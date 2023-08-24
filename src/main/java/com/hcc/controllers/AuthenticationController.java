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
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;


@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Endpoint to log in a user.
     *
     * @param authCredentialRequest The credentials for the login.
     * @return The AuthCredentialResponseDto response.
     */
    @PostMapping("/auth/login")
    public ResponseEntity<AuthCredentialResponseDto> login(
            @RequestBody AuthCredentialRequestDto authCredentialRequest) {
        logger.info("login request");
        logger.info(authCredentialRequest.toString());
        try {
            //I wanna see the little loading spinner spin for dramatic effect
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        return ResponseEntity.ok(authenticationService.login(authCredentialRequest));

    }

    /**
     * Endpoint to validate the provided token.
     * If this method is called, it means that the provided token has already been authenticated by Spring Security.
     *
     * @param token The token to validate.
     * @param user The authenticated user.
     * @return True if token is valid, false otherwise.
     */
    @GetMapping("/auth/validate")
    public ResponseEntity<Boolean> validate(
            @RequestHeader("Authorization") String token, @AuthenticationPrincipal User user) {
        logger.info("validated token");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        return ResponseEntity.ok(true);
    }

    public AuthenticationController() {

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleIncorrectCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

}
