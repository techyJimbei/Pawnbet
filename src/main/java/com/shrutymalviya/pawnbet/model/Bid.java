package com.shrutymalviya.pawnbet.model;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="bids")
public class Bid extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private BigDecimal bidAmount;

    @ManyToOne
    private Product product;

    @ManyToOne
    private User bidder;

    private boolean accepted;


}
