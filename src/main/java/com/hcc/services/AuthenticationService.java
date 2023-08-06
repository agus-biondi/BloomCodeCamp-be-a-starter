package com.hcc.services;

import com.hcc.entities.User;
import com.hcc.dtos.AuthCredentialRequestDto;
import com.hcc.dtos.AuthCredentialResponseDto;
import com.hcc.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailServiceImpl userDetailService;

    public AuthCredentialResponseDto login(AuthCredentialRequestDto request) throws Exception{

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    ));
        } catch (UsernameNotFoundException e) {
            throw new Exception("Username not found", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid Credentials", e);
        }

        User user = (User) userDetailService.loadUserByUsername(request.getUsername());
        user.setPassword(null);
        String token = jwtUtil.generateToken(user);
        return new AuthCredentialResponseDto(token);
    }

    public boolean validate() {
        //jwtUtil.validateToken()
        return false;
    }
}
