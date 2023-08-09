package com.hcc.entities;

import com.hcc.enums.AssignmentEnum;
import com.hcc.enums.AssignmentStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "assignments")
@Getter
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AssignmentStatusEnum status;

    @Column(name = "number")
    @Enumerated(EnumType.ORDINAL)
    private AssignmentEnum number;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "branch")
    private String branch;

    @Column(name = "code_review_video_url")
    private String reviewVideoUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "code_reviewer_id")
    private User codeReviewer;

    public Assignment() {

    }

    public Assignment(AssignmentStatusEnum status, AssignmentEnum number, String githubUrl, String branch, String reviewVideoUrl, User user, User codeReviewer) {
        this.status = status;
        this.number = number;
        this.githubUrl = githubUrl;
        this.branch = branch;
        this.reviewVideoUrl = reviewVideoUrl;
        this.user = user;
        this.codeReviewer = codeReviewer;
    }

    public Long getId() {
        return id;
    }


    public void setStatus(AssignmentStatusEnum status) {
        this.status = status;
    }
}
