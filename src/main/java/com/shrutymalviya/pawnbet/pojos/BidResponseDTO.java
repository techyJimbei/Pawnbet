package com.shrutymalviya.pawnbet.pojos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BidResponseDTO {
    private long id;
    private BigDecimal bidAmount;
    private UserResponseDTO bidder;
    private boolean accepted;
    private LocalDateTime createdAt;
}
