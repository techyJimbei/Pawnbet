package com.shrutymalviya.pawnbet.pojos;

import com.shrutymalviya.pawnbet.model.Image;
import com.shrutymalviya.pawnbet.model.Product;
import com.shrutymalviya.pawnbet.model.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
public class ProductResponseDTO {
    private long id;
    private String title;
    private String description;
    private BigDecimal basePrice;
    private ProductStatus status;
    private UserResponseDTO seller;
    private List<String> imageUrls;
    private LocalDateTime createdAt;

    public ProductResponseDTO(Product saved) {
        this.id = saved.getId();
        this.title = saved.getTitle();
        this.description = saved.getDescription();
        this.basePrice = saved.getBasePrice();
        this.status = saved.getStatus();
        this.imageUrls = saved.getImages().stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());
        this.seller = new UserResponseDTO(saved.getSeller());
        this.createdAt = saved.getCreated();
    }


}
