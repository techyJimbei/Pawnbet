package com.shrutymalviya.pawnbet.repositrory;

import com.shrutymalviya.pawnbet.model.Auction;
import com.shrutymalviya.pawnbet.model.Product;
import com.shrutymalviya.pawnbet.pojos.AuctionScheduleResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    Auction findByProduct(Product product);
}
