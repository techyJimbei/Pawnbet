package com.shrutymalviya.pawnbet.pojos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductRequestDTO {
    private String title;
    private String description;
    private String tag;
    private BigDecimal basePrice;
    private String imageUrls;
}
