package com.shrutymalviya.pawnbet.repositrory;

import com.shrutymalviya.pawnbet.model.Auction;
import com.shrutymalviya.pawnbet.model.Product;
import com.shrutymalviya.pawnbet.model.User;
import com.shrutymalviya.pawnbet.pojos.ProductResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    Auction findByProduct(Product product);

    List<Auction> findByWinningBidder(User winningBidder);
}
