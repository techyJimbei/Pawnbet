package com.shrutymalviya.pawnbet.pojos;

import com.shrutymalviya.pawnbet.model.Wishlist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishlistResponseDTO {
    public Long id;
    public UserResponseDTO user;
    public ProductResponseDTO product;

    public WishlistResponseDTO(Wishlist wishlist) {
        this.id = wishlist.getId();
        this.user = new UserResponseDTO(wishlist.getUser());
        this.product = new ProductResponseDTO(wishlist.getProduct());
    }
}
