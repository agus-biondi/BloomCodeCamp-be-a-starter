package com.hcc.dtos;

public class AuthCredentialResponseDto {

    private String token;

    public AuthCredentialResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
