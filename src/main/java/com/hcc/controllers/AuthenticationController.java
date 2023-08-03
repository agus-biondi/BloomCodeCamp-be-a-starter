package com.hcc.controllers;

import com.hcc.dtos.AuthCredentialRequestDto;
import com.hcc.dtos.AuthCredentialResponseDto;
import com.hcc.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/auth/login")
    public ResponseEntity<AuthCredentialResponseDto> login(
            @RequestBody AuthCredentialRequestDto authCredentialRequest
    ) throws Exception {

        return ResponseEntity.ok(authenticationService.login(authCredentialRequest));

    }

    @PostMapping("/auth/validate")
    public ResponseEntity<Boolean> validate(
            //@RequestBody
    ) throws Exception {
        return ResponseEntity.ok(authenticationService.validate());
    }

    public AuthenticationController() {

    }


}
