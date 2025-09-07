package com.shrutymalviya.pawnbet.service;


import com.shrutymalviya.pawnbet.model.Bid;
import com.shrutymalviya.pawnbet.model.Order;
import com.shrutymalviya.pawnbet.model.Product;
import com.shrutymalviya.pawnbet.model.User;
import com.shrutymalviya.pawnbet.pojos.OrderRequestDTO;
import com.shrutymalviya.pawnbet.pojos.OrderResponseDTO;
import com.shrutymalviya.pawnbet.repositrory.BidRepository;
import com.shrutymalviya.pawnbet.repositrory.OrderRepository;
import com.shrutymalviya.pawnbet.repositrory.ProductRepository;
import com.shrutymalviya.pawnbet.repositrory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BidRepository bidRepository;

    public OrderResponseDTO addOrder(OrderRequestDTO orderRequestDTO) {

        Product product = productRepository.findById(orderRequestDTO.productId).orElseThrow(() -> new RuntimeException("Product not found"));

        if (orderRepository.existsByProduct(product)) {
            return new OrderResponseDTO(
                    orderRepository.findByProduct(product)
                            .orElseThrow(() -> new RuntimeException("Order not found"))
            );
        }


        Bid bid = bidRepository.findById(orderRequestDTO.winnerBidId).orElseThrow(() -> new RuntimeException("Bid not found"));

        Order order = new Order();
        order.setProduct(product);
        order.setWinningBid(bid);
        order.setPaid(false);
        order.setSeller(product.getSeller());

        Order saved = orderRepository.save(order);

        return new OrderResponseDTO(saved);
    }

    public List<OrderResponseDTO> getOrders(String username){
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));

        List<Order> orders = orderRepository.findAllBySeller(user);
        return orders.stream().map(OrderResponseDTO::new).toList();
    }

    public OrderResponseDTO addPayment(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Order order = orderRepository.findByProduct(product)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setPaid(true);
        Order updated = orderRepository.save(order);

        return new OrderResponseDTO(updated);
    }


}
