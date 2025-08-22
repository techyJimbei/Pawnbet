package com.shrutymalviya.pawnbet.pojos;

import com.shrutymalviya.pawnbet.model.AuctionStatus;
import com.shrutymalviya.pawnbet.model.Product;
import com.shrutymalviya.pawnbet.model.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
public class ProductResponseDTO {
    private long id;
    private String title;
    private String description;
    private String tag;
    private BigDecimal basePrice;
    private ProductStatus productStatus;
    private AuctionStatus auctionStatus;
    private UserResponseDTO seller;
    private String imageUrl;
    private LocalDateTime createdAt;

    public ProductResponseDTO(Product saved) {
        this.id = saved.getId();
        this.title = saved.getTitle();
        this.description = saved.getDescription();
        this.tag = saved.getTag();
        this.basePrice = saved.getBasePrice();
        this.productStatus = saved.getProductStatus();
        this.auctionStatus = saved.getAuctionStatus();
        this.imageUrl = saved.getImage();
        this.seller = new UserResponseDTO(saved.getSeller());
        this.createdAt = saved.getCreated();
    }


}
