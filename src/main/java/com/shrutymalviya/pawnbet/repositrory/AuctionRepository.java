package com.shrutymalviya.pawnbet.repositrory;

import com.shrutymalviya.pawnbet.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
