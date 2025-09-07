package com.shrutymalviya.pawnbet.pojos;

import com.shrutymalviya.pawnbet.model.Order;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderResponseDTO {
    private Long id;
    private ProductResponseDTO product;
    private BidResponseDTO winningBid;
    private boolean isPaid;

    public OrderResponseDTO(Order order) {
        this.id = order.getId();
        this.product = new ProductResponseDTO(order.getProduct());
        this.winningBid = new BidResponseDTO(order.getWinningBid());
        this.isPaid = order.isPaid();
    }
}

