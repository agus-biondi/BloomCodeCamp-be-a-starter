package com.hcc.services;

import com.hcc.controllers.AuthenticationController;
import com.hcc.entities.User;
import com.hcc.dtos.AuthCredentialRequestDto;
import com.hcc.dtos.AuthCredentialResponseDto;
import com.hcc.utils.CustomPasswordEncoder;
import com.hcc.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomPasswordEncoder customPasswordEncoder;
    @Autowired
    UserDetailServiceImpl userDetailService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthCredentialResponseDto login(AuthCredentialRequestDto request) throws UsernameNotFoundException, BadCredentialsException{
        logger.info("auth service logging in");
        logger.info(String.format("user: %s password: %s", request.getUsername(), request.getPassword()));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));
        User user = (User) userDetailService.loadUserByUsername(request.getUsername());
        user.setPassword(null);
        String token = jwtUtil.generateToken(user);
        return new AuthCredentialResponseDto(token);
    }

    public boolean validate(String token, User authenticatedUser) {
        String authenticatedUsername = authenticatedUser.getUsername();
        UserDetails userDetails = userDetailService.loadUserByUsername(authenticatedUsername);
        return jwtUtil.validateToken(token, userDetails);
    }
}
