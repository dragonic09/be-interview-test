package com.backend.interviewtest.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="user_accounts")
public class UserAccount {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "password")
    private String password;
}
