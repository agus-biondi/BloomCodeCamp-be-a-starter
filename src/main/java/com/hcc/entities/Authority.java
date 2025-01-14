package com.hcc.entities;

import com.hcc.enums.AuthorityEnum;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    private AuthorityEnum authority;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Authority() {

    }
    public Authority(User user, AuthorityEnum authority) {
        this.user = user;
        this.authority = authority;
    }

    public Authority(AuthorityEnum authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority.name();
    }


}
