package com.shrutymalviya.pawnbet.service;

import com.shrutymalviya.pawnbet.model.Auction;
import com.shrutymalviya.pawnbet.model.AuctionStatus;
import com.shrutymalviya.pawnbet.model.Bid;
import com.shrutymalviya.pawnbet.model.Product;
import com.shrutymalviya.pawnbet.pojos.OrderRequestDTO;
import com.shrutymalviya.pawnbet.repositrory.AuctionRepository;
import com.shrutymalviya.pawnbet.repositrory.BidRepository;
import com.shrutymalviya.pawnbet.repositrory.ProductRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AuctionScheduler {

    private final ProductRepository productRepository;
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final OrderService orderService;

    public AuctionScheduler(ProductRepository productRepository,
                            BidRepository bidRepository,
                            AuctionRepository auctionRepository,
                            OrderService orderService) {
        this.productRepository = productRepository;
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
        this.orderService = orderService;
    }

    // Run every 1 minute
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkEndedAuctions() {
        LocalDateTime now = LocalDateTime.now();

        // Fetch all auctions that have ended but not yet processed
        List<Auction> endedAuctions = auctionRepository.findAll().stream()
                .filter(a -> a.getEndTime() != null
                        && now.isAfter(a.getEndTime())
                        && a.getWinningBidder() == null)
                .toList();

        for (Auction auction : endedAuctions) {
            Product product = auction.getProduct();

            // Find the highest bid for this product
            Bid highestBid = bidRepository.findTopByProductOrderByBidAmountDesc(product);

            if (highestBid != null) {
                // Create an order for the winning bid
                orderService.addOrder(new OrderRequestDTO(product.getId(), highestBid.getId()));

                // Set winning bidder and update auction/product status
                auction.setWinningBidder(highestBid.getBidder());
                product.setAuctionStatus(AuctionStatus.ORDER_CREATED);
            } else {
                // No bids, mark auction as ended
                product.setAuctionStatus(AuctionStatus.ENDED);
            }

            // Save updates
            auctionRepository.save(auction);
            productRepository.save(product);
        }
    }
}
