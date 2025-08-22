package com.shrutymalviya.pawnbet.pojos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductUpdateDTO {
    private String title;
    private String description;
    private BigDecimal basePrice;
    private String imageUrl;
}
