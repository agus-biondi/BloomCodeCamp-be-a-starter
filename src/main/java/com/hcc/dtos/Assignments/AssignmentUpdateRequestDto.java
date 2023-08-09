package com.hcc.dtos.Assignments;

import com.hcc.enums.AssignmentStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssignmentUpdateRequestDto {
    private String githubUrl;
    private String branch;
    private AssignmentStatusEnum status;
    private String reviewVideoUrl;
}
