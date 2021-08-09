package com.backend.interviewtest.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="access_tokens")
public class AccessToken {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "publish_at")
    private Date publishAt;
}
