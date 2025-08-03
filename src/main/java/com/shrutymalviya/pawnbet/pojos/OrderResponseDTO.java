package com.shrutymalviya.pawnbet.pojos;

import java.time.LocalDateTime;

public class OrderResponseDTO {
    private Long id;
    private ProductResponseDTO product;
    private BidResponseDTO winningBid;
    private boolean isPaid;
    private LocalDateTime createdAt;
}

