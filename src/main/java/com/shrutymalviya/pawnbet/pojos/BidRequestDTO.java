package com.shrutymalviya.pawnbet.pojos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BidRequestDTO {
    private BigDecimal bidAmount;
    private long productId;
}
