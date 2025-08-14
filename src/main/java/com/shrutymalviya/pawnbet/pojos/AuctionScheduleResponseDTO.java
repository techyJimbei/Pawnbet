package com.shrutymalviya.pawnbet.pojos;

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
    private Long winningBidderId;
    private LocalDateTime auctionStartTime;
    private LocalDateTime auctionEndTime;

    public AuctionScheduleResponseDTO(Long id,
                                      LocalDateTime auctionEndTime,
                                      LocalDateTime auctionStartTime,
                                      Long winningBidderId,
                                      BigDecimal basePrice,
                                      Long productId
    )
    {
        this.id = id;
        this.auctionEndTime = auctionEndTime;
        this.auctionStartTime = auctionStartTime;
        this.winningBidderId = winningBidderId;
        this.basePrice = basePrice;
        this.productId = productId;
    }


}
