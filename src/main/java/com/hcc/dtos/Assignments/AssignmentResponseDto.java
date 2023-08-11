package com.hcc.dtos.Assignments;

import com.hcc.entities.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AssignmentResponseDto {
    private Long id;
    private String status;
    private User user;

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
    }
}


