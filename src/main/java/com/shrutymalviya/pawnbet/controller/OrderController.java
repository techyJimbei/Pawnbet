package com.shrutymalviya.pawnbet.controller;


import com.shrutymalviya.pawnbet.pojos.OrderRequestDTO;
import com.shrutymalviya.pawnbet.pojos.OrderResponseDTO;
import com.shrutymalviya.pawnbet.pojos.ProductRequestDTO;
import com.shrutymalviya.pawnbet.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<?> addOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        try{
            OrderResponseDTO orderResponseDTO = orderService.addOrder(orderRequestDTO);
            return ResponseEntity.ok(orderResponseDTO);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/order")
    public ResponseEntity<?> getOrders(Authentication authentication) {
        try {
            String username = authentication.getName();
            List<OrderResponseDTO> orders = orderService.getOrders(username);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PostMapping("/pay/{productId}")
    public OrderResponseDTO addPayment(@PathVariable Long productId) {
        return orderService.addPayment(productId);
    }


}
