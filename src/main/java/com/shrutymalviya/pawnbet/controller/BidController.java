package com.shrutymalviya.pawnbet.controller;

import com.shrutymalviya.pawnbet.model.Bid;
import com.shrutymalviya.pawnbet.pojos.BidRequestDTO;
import com.shrutymalviya.pawnbet.pojos.BidResponseDTO;
import com.shrutymalviya.pawnbet.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BidController {

    @Autowired
    private BidService bidService;

    @PostMapping("/bid/{product_id}/{bidder_id}")
    public ResponseEntity<?> raiseBid(@RequestBody BidRequestDTO bidRequestDTO, @PathVariable long product_id, @PathVariable long bidder_id) {
        try{
            BidResponseDTO bidResponseDTO = bidService.raiseBid(bidRequestDTO, product_id, bidder_id);
            return ResponseEntity.ok(bidResponseDTO);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Failed to raise bid "+e.getMessage());
        }

    }

    @GetMapping("/bid/{product_id}")
    public ResponseEntity<?> getBids(@PathVariable long product_id){
        try{
            List<BidResponseDTO> bids = bidService.getBids(product_id);
            return ResponseEntity.ok(bids);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Failed to get bids" + e.getMessage());
        }
    }

    @PutMapping("/bid/{bid_id}")
    public ResponseEntity<?> updateBid(@RequestBody BidRequestDTO bidRequestDTO, @PathVariable long bid_id){
        try{
            BidResponseDTO bidResponseDTO = bidService.updateBid(bidRequestDTO, bid_id);
            return ResponseEntity.ok(bidResponseDTO);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Failed to update bid "+e.getMessage());
        }
    }

    @DeleteMapping("/bid/{bid_id}/{bidder_id}")
    public String deleteBid(@PathVariable long bid_id, @PathVariable long bidder_id){
        try{
            bidService.deleteBid(bid_id, bidder_id);
            return "bid deleted";
        }
        catch(Exception e){
            throw new RuntimeException("bid cannot be deleted" + e.getMessage());
        }

    }

}
