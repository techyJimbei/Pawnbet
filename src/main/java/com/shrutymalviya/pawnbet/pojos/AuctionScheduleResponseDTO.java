package com.shrutymalviya.pawnbet.pojos;

import com.shrutymalviya.pawnbet.model.Auction;
import com.shrutymalviya.pawnbet.model.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
public class AuctionScheduleResponseDTO {
    private Long id;
    private Long productId;
    private BigDecimal basePrice;
    private User winningBidder;
    private String auctionStartTime;
    private String auctionEndTime;

    public AuctionScheduleResponseDTO(Auction auction)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        this.id = auction.getId();
        this.auctionEndTime = auction.getEndTime().format(formatter);
        this.auctionStartTime = auction.getStartTime().format(formatter);
        this.winningBidder = auction.getWinningBidder();
        this.basePrice = auction.getProduct().getBasePrice();
        this.productId = auction.getProduct().getId();
    }

}
