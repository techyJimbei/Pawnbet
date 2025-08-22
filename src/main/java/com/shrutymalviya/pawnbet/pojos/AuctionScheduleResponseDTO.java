package com.shrutymalviya.pawnbet.pojos;

import com.shrutymalviya.pawnbet.model.Auction;
import com.shrutymalviya.pawnbet.model.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
public class AuctionScheduleResponseDTO {
    private Long id;
    private Long productId;
    private BigDecimal basePrice;
    private User winningBidder;
    private LocalDateTime auctionStartTime;
    private LocalDateTime auctionEndTime;

    public AuctionScheduleResponseDTO(Auction auction)
    {
        this.id = auction.getId();
        this.auctionEndTime = auction.getEndTime();
        this.auctionStartTime = auction.getStartTime();
        this.winningBidder = auction.getWinningBidder();
        this.basePrice = auction.getProduct().getBasePrice();
        this.productId = auction.getProduct().getId();
    }

}
