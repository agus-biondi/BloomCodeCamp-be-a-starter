package com.hcc.dtos.Assignments;

import com.hcc.enums.AssignmentStatusEnum;
import com.hcc.enums.AuthorityEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AssignmentUpdateRequestDto {
    private String githubUrl;
    private String branch;
    private AssignmentStatusEnum status;
    private String reviewVideoUrl;

    private AuthorityEnum updateAssignmentAsRole;

    public AuthorityEnum getUpdateAssignmentAsRole() {
        return updateAssignmentAsRole;
    }

    public void setUpdateAssignmentAsRole(AuthorityEnum updateAssignmentAsRole) {
        this.updateAssignmentAsRole = updateAssignmentAsRole;
    }
    public String getGithubUrl() {
        return githubUrl;
    }

    public String getBranch() {
        return branch;
    }

    public AssignmentStatusEnum getStatus() {
        return status;
    }

    public String getReviewVideoUrl() {
        return reviewVideoUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setStatus(AssignmentStatusEnum status) {
        this.status = status;
    }

    public void setReviewVideoUrl(String reviewVideoUrl) {
        this.reviewVideoUrl = reviewVideoUrl;
    }
}
