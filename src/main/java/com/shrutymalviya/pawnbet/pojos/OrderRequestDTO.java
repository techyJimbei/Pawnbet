package com.shrutymalviya.pawnbet.pojos;

public class OrderRequestDTO {
    public Long productId;
    public Long winnerBidId;

    public OrderRequestDTO(Long productId, Long winnerBidId) {
        this.productId = productId;
        this.winnerBidId = winnerBidId;
    }
}
