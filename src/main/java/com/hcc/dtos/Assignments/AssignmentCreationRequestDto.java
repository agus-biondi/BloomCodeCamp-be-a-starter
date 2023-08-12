package com.hcc.dtos.Assignments;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AssignmentCreationRequestDto {
    private String number;
    private String githubUrl;
    private String branch;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return "AssignmentCreationRequestDto{" +
                "number='" + number + '\'' +
                ", githubUrl='" + githubUrl + '\'' +
                ", branch='" + branch + '\'' +
                '}';
    }
}
