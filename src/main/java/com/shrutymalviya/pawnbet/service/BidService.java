package com.shrutymalviya.pawnbet.service;

import com.shrutymalviya.pawnbet.model.Bid;
import com.shrutymalviya.pawnbet.model.Product;
import com.shrutymalviya.pawnbet.model.User;
import com.shrutymalviya.pawnbet.pojos.BidRequestDTO;
import com.shrutymalviya.pawnbet.pojos.BidResponseDTO;
import com.shrutymalviya.pawnbet.repositrory.BidRepository;
import com.shrutymalviya.pawnbet.repositrory.ProductRepository;
import com.shrutymalviya.pawnbet.repositrory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    public BidResponseDTO raiseBid(BidRequestDTO bidRequestDTO, long product_id, long bidder_id) throws RuntimeException {
        User bidder = userRepository.findById(bidder_id).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Product product = productRepository.findById(product_id).orElseThrow(() -> new RuntimeException("Product not found"));

        Bid bid = new Bid();
        bid.setBidder(bidder);
        boolean isGreaterThan = (bidRequestDTO.getBidAmount().compareTo(product.getBasePrice()) > 0);
        if (isGreaterThan) {
            bid.setBidAmount(bidRequestDTO.getBidAmount());
        } else {
            throw new RuntimeException("Bid Amount is less than the Base price");
        }
        bid.setProduct(product);
        bid.setAccepted(false);

        Bid saved = bidRepository.save(bid);
        return new BidResponseDTO(saved);
    }


    public List<BidResponseDTO> getBids(long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        List<Bid> bids = bidRepository.findByProduct(product);

        return bids.stream().map(BidResponseDTO::new).collect(Collectors.toList());
    }

    public BidResponseDTO updateBid(BidRequestDTO bidRequestDTO, long bid_id, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Bid bid = bidRepository.findById(bid_id).orElseThrow(() -> new RuntimeException("Bid does not exist"));

        if (!bid.getBidder().equals(user)) throw new RuntimeException("Unauthorized");

        if (bidRequestDTO.getBidAmount() != null && bidRequestDTO.getBidAmount().compareTo(bid.getBidAmount()) > 0) {
            bid.setBidAmount(bidRequestDTO.getBidAmount());
        } else {
            throw new RuntimeException("Bid Amount is less than the prev Bid Amount");
        }

        Bid updated = bidRepository.save(bid);

        return new BidResponseDTO(updated);

    }

    public void deleteBid(long bid_id, String username) {
        Bid bid = bidRepository.findById(bid_id).orElseThrow(() -> new RuntimeException("bid cannot be found"));
        User bidder = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        if (bid.getBidder().equals(bidder)) {
            bidRepository.delete(bid);
        } else {
            throw new RuntimeException("Not eligible to be deleting this bid");
        }
    }

    public void acceptBid(Long bidId, String username) {
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new RuntimeException("Bid not found"));

        if (!bid.getProduct().getSeller().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }

        bid.setAccepted(true);
        bidRepository.save(bid);
    }

}
