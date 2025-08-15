package com.shrutymalviya.pawnbet.pojos;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AuctionScheduleRequestDTO {
    private LocalDateTime auctionStartTime;
    private LocalDateTime auctionEndTime;
}
