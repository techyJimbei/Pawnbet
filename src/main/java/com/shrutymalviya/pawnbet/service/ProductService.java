package com.shrutymalviya.pawnbet.service;


import com.shrutymalviya.pawnbet.model.*;
import com.shrutymalviya.pawnbet.pojos.*;
import com.shrutymalviya.pawnbet.repositrory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private OrderRepository orderRepository;


    @Transactional
    public ProductResponseDTO listProduct(ProductRequestDTO productRequestDTO, String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Product product = new Product();

        product.setTitle(productRequestDTO.getTitle());
        product.setDescription(productRequestDTO.getDescription());
        product.setTag(productRequestDTO.getTag());
        product.setBasePrice(productRequestDTO.getBasePrice());
        product.setProductStatus(ProductStatus.ACTIVE);
        product.setAuctionStatus(AuctionStatus.YET_TO_DECLARE);
        product.setSeller(user);
        product.setImage(productRequestDTO.getImageUrl());

        Product saved = productRepository.save(product);
        return new ProductResponseDTO(saved);

    }

    public List<ProductResponseDTO> getMyProducts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Product> products = productRepository.findBySeller(user);

        return products.stream()
                .map(product -> {

                    AuctionStatus auctionStatus = AuctionStatus.valueOf(calculateAuctionStatus(product, product.getAuction()));

                    ProductResponseDTO dto = new ProductResponseDTO(product);
                    dto.setAuctionStatus(auctionStatus);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getAllProducts(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Product> products = productRepository.findAll();

        Set<Long> wishlistedProductIds = wishlistRepository.findProductIdsByUser(user);


        return products.stream()
                .map(product -> {

                    AuctionStatus auctionStatus = AuctionStatus.valueOf(calculateAuctionStatus(product, product.getAuction()));

                    ProductResponseDTO dto = new ProductResponseDTO(product);
                    dto.setIsWishlisted(wishlistedProductIds.contains(product.getId()));
                    dto.setAuctionStatus(auctionStatus);
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public ProductResponseDTO updateProduct(long productId, ProductUpdateDTO productUpdateDTO, String username) throws RuntimeException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not Found"));

        if (!product.getSeller().getUsername().equals(username)) {
            throw new RuntimeException("Not allowed to update product");
        }

        if (productUpdateDTO.getTitle() != null) product.setTitle(productUpdateDTO.getTitle());
        if (productUpdateDTO.getDescription() != null) product.setDescription(productUpdateDTO.getDescription());
        if (productUpdateDTO.getBasePrice() != null) product.setBasePrice(productUpdateDTO.getBasePrice());
        if (productUpdateDTO.getImageUrl() != null) product.setImage(productUpdateDTO.getImageUrl());


        Product updatedProduct = productRepository.save(product);
        return new ProductResponseDTO(updatedProduct);
    }

    public void deleteProduct(long productId, String username) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not Found"));

        if (!product.getSeller().getUsername().equals(username)) {
            throw new RuntimeException("Not allowed to delete product");
        }
        productRepository.delete(product);
    }

    public List<ProductResponseDTO> getTrendingProducts() {
        Pageable topTen = PageRequest.of(0, 10);
        List<Product> trendingProducts = productRepository.findTop10TrendingProducts(topTen);
        return trendingProducts.stream().map(ProductResponseDTO::new).toList();
    }

    public List<ProductResponseDTO> searchProducts(String keyword) {
        List<Product> products = productRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrTagContainingIgnoreCase(keyword, keyword, keyword);
        return products.stream().map(ProductResponseDTO::new).collect(Collectors.toList());
    }

    public AuctionScheduleResponseDTO addAuctionDetails(long productId, String username, AuctionScheduleRequestDTO auctionScheduleRequestDTO) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not Found"));

        Auction auction = new Auction();
        auction.setProduct(product);
        auction.setStartTime(auctionScheduleRequestDTO.getAuctionStartTime());
        auction.setEndTime(auctionScheduleRequestDTO.getAuctionEndTime());

        product.setAuctionStatus(AuctionStatus.DETAILS_ADDED);

        Auction saved = auctionRepository.save(auction);
        return new AuctionScheduleResponseDTO(saved);
    }

    public AuctionScheduleResponseDTO getAuctionDetails(String username, Long productId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not Found"));

        Auction auction = auctionRepository.findByProduct(product);
        if (auction == null) {
            throw new RuntimeException("Auction not found for product");
        }

        return new AuctionScheduleResponseDTO(auction);
    }

    public List<ProductResponseDTO> getWinningAuctions(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Auction> endedAuctions = auctionRepository.findAll().stream()
                .filter(a -> a.getEndTime() != null && LocalDateTime.now().isAfter(a.getEndTime()))
                .toList();

        for (Auction auction : endedAuctions) {
            createOrderIfNotExists(auction.getProduct(), auction);
        }

        List<Auction> wonAuctions = auctionRepository.findByWinningBidder(user);

        return wonAuctions.stream()
                .map(a -> new ProductResponseDTO(a.getProduct()))
                .toList();
    }



    private String calculateAuctionStatus(Product product, Auction auction) {
        if (auction == null
                || auction.getStartTime() == null
                || auction.getEndTime() == null) {
            return "YET_TO_DECLARE";
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(auction.getStartTime())) {
            return "DETAILS_ADDED";
        } else if (now.isAfter(auction.getEndTime())) {
            createOrderIfNotExists(product, auction);
            return "ENDED";
        } else {
            return "LIVE";
        }
    }

    private void createOrderIfNotExists(Product product, Auction auction) {
        boolean exists = orderRepository.existsByProduct(product);
        if (!exists) {
            Bid highestBid = bidRepository.findTopByProductOrderByBidAmountDesc(product);

            if (highestBid != null) {

                OrderRequestDTO dto = new OrderRequestDTO(product.getId(), highestBid.getId());
                orderService.addOrder(dto);

                auction.setWinningBidder(highestBid.getBidder());
                auctionRepository.save(auction);

                product.setAuctionStatus(AuctionStatus.ORDER_CREATED);
                productRepository.save(product);
            } else {
                product.setAuctionStatus(AuctionStatus.ENDED);
                productRepository.save(product);
            }
        }
    }


}
