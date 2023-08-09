package com.hcc.dtos.Assignments;

import com.hcc.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssignmentResponseDto {
    private Long id;
    private String status;
    private User user;
}
