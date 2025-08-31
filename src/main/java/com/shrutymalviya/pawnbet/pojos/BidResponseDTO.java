package com.shrutymalviya.pawnbet.pojos;

import com.shrutymalviya.pawnbet.model.Bid;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class BidResponseDTO {
    private Long id;
    private BigDecimal bidAmount;
    private UserResponseDTO bidder;
    private ProductResponseDTO product;
    private Boolean accepted;
    private LocalDateTime createdAt;

    public BidResponseDTO(Bid saved){
        this.id = saved.getId();
        this.bidAmount = saved.getBidAmount();
        this.bidder = new UserResponseDTO(saved.getBidder());
        this.product = new ProductResponseDTO(saved.getProduct());
        this.accepted = saved.isAccepted();
        this.createdAt = LocalDateTime.now();
    }

}
