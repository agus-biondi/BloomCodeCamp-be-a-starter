package com.hcc.utils;

import com.hcc.dtos.Assignments.AssignmentCreationRequestDto;
import com.hcc.dtos.Assignments.AssignmentResponseDto;
import com.hcc.entities.Assignment;
import com.hcc.entities.User;
import com.hcc.enums.AssignmentEnum;
import com.hcc.enums.AssignmentStatusEnum;
import org.springframework.stereotype.Component;

@Component
public class AssignmentMapper {
    public static Assignment creationDtoToEntity(AssignmentCreationRequestDto dto, User user) {
        return new Assignment(
                null,
                AssignmentEnum.valueOf(dto.getNumber()),
                dto.getGithubUrl(),
                dto.getBranch(),
                null,
                user,
                null
        );
    }

    public static AssignmentResponseDto toResponseDto(Assignment assignment) {
        AssignmentResponseDto dto = new AssignmentResponseDto();
        dto.setId(assignment.getId());
        dto.setStatus(assignment.getStatus().name());
        return dto;
    }
}
