package com.shrutymalviya.pawnbet.model;


import jakarta.persistence.*;

@Entity
@Table(name="images")
public class Image extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String imageUrl;

    @ManyToOne
    private Product product;
}
