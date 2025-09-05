package com.shrutymalviya.pawnbet.controller;

import com.shrutymalviya.pawnbet.model.Bid;
import com.shrutymalviya.pawnbet.pojos.BidRequestDTO;
import com.shrutymalviya.pawnbet.pojos.BidResponseDTO;
import com.shrutymalviya.pawnbet.repositrory.BidRepository;
import com.shrutymalviya.pawnbet.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BidController {

    @Autowired
    private BidService bidService;

    @Autowired
    private BidRepository bidRepository;

    @PostMapping("/bid/{product_id}")
    public ResponseEntity<?> raiseBid(@RequestBody BidRequestDTO bidRequestDTO, @PathVariable long product_id, Authentication authentication) {
        try{
            String username = authentication.getName();
            BidResponseDTO bidResponseDTO = bidService.raiseBid(bidRequestDTO, product_id, username);
            return ResponseEntity.ok(bidResponseDTO);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Failed to raise bid "+e.getMessage());
        }

    }


    @GetMapping("/bid/{product_id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getBids(@PathVariable long product_id) {
        try{
            List<BidResponseDTO> bids = bidService.getBids(product_id);
            return ResponseEntity.ok(bids);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Failed to get bids" + e.getMessage());
        }
    }

    @GetMapping("/bid/highest/{product_id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getHighestBid(@PathVariable long product_id) {
        try{
            BidResponseDTO bid = bidService.getHighestBid(product_id);
            return ResponseEntity.ok(bid);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get highest bid" + e.getMessage());
        }
    }

    @PutMapping("/bid/{bid_id}")
    public ResponseEntity<?> updateBid(@RequestBody BidRequestDTO bidRequestDTO, @PathVariable long bid_id, Authentication authentication){
        try{
            String username = authentication.getName();
            BidResponseDTO bidResponseDTO = bidService.updateBid(bidRequestDTO, bid_id, username);
            return ResponseEntity.ok(bidResponseDTO);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Failed to update bid "+e.getMessage());
        }
    }

    @DeleteMapping("/bid/{bid_id}")
    public String deleteBid(@PathVariable long bid_id, Authentication authentication){
        try{
            String username = authentication.getName();
            bidService.deleteBid(bid_id, username);
            return "bid deleted";
        }
        catch(Exception e){
            throw new RuntimeException("bid cannot be deleted" + e.getMessage());
        }

    }

    @PutMapping("/bid/{bidId}/accept")
    public ResponseEntity<?> acceptBid(@PathVariable Long bidId, Authentication authentication) {
        String username = authentication.getName();
        bidService.acceptBid(bidId, username);
        return ResponseEntity.ok("Bid accepted");
    }

    @GetMapping("/bid/my")
    public ResponseEntity<?> getMyBids(Authentication authentication) {
        String username = authentication.getName();
        List<Bid> active = bidRepository.findByBidderUsernameAndAccepted(username, false);
        List<Bid> accepted = bidRepository.findByBidderUsernameAndAccepted(username, true);
        return ResponseEntity.ok(Map.of("active", active, "accepted", accepted));
    }



}
