package com.shrutymalviya.pawnbet.repositrory;

import com.shrutymalviya.pawnbet.model.Bid;
import com.shrutymalviya.pawnbet.model.Product;
import com.shrutymalviya.pawnbet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByProduct(Product product);

    Bid findTopByProductOrderByBidAmountDesc(Product product);

    List<Bid> bidder(User bidder);

    List<Bid> findByBidderUsername(String username);

    List<Bid> findByBidderUsernameAndAccepted(String username, boolean accepted);

}
