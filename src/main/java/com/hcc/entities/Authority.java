package com.hcc.entities;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "authority")
    private String authority;

    @ManyToOne
    @Column(name = "user_id")
    private User user;

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }


}
