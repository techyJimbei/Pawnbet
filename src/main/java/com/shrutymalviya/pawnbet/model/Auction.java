package com.shrutymalviya.pawnbet.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "auctions",
        indexes = {
        @Index(name = "idx_product_id", columnList = "product_id"),
        @Index(name = "idx_winning_bidder_id", columnList = "winning_bidder_id")}
)
@Getter
@Setter
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal minimumBidIncrement;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name = "winning_bidder_id")
    private User winningBidder;
}
