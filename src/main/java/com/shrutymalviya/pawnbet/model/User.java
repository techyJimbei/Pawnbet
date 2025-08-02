package com.shrutymalviya.pawnbet.model;


import jakarta.persistence.*;

@Entity
@Table(name="user")
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String email;

    private String username;
    private String password;

    private String profileImageUrl;


}
