package com.hcc.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {

    private boolean success;
    private String message;
    private Object data;

}
