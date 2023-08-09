package com.hcc.dtos.Assignments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssignmentCreationRequestDto {
    private String number;
    private String githubUrl;
    private String branch;
}
