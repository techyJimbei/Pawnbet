package com.shrutymalviya.pawnbet.model;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Product product;

    @OneToOne
    private Bid winningBid;

    private boolean isPaid;

}

