package com.shrutymalviya.pawnbet.service;


import com.shrutymalviya.pawnbet.model.*;
import com.shrutymalviya.pawnbet.pojos.AuctionScheduleRequestDTO;
import com.shrutymalviya.pawnbet.pojos.ProductRequestDTO;
import com.shrutymalviya.pawnbet.pojos.ProductResponseDTO;
import com.shrutymalviya.pawnbet.pojos.ProductUpdateDTO;
import com.shrutymalviya.pawnbet.repositrory.AuctionRepository;
import com.shrutymalviya.pawnbet.repositrory.ProductRepository;
import com.shrutymalviya.pawnbet.repositrory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Transactional
    public ProductResponseDTO listProduct(ProductRequestDTO productRequestDTO, String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Product product = new Product();

        product.setTitle(productRequestDTO.getTitle());
        product.setDescription(productRequestDTO.getDescription());
        product.setBasePrice(productRequestDTO.getBasePrice());
        product.setProductStatus(ProductStatus.ACTIVE);
        product.setAuctionStatus(AuctionStatus.YET_TO_DECLARE);
        product.setSeller(user);

        List<Image> images = (productRequestDTO.getImageUrls() != null)
                ? productRequestDTO.getImageUrls().stream()
                .map(url -> {
                    Image image = new Image();
                    image.setImageUrl(url);
                    image.setProduct(product);
                    return image;
                }).collect(Collectors.toList())
                : List.of();

        product.setImages(images);

        Product saved = productRepository.save(product);
        return new ProductResponseDTO(saved);

    }

    public List<ProductResponseDTO> fetchProducts(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Product> products = productRepository.findBySeller(user);

        return products.stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO updateProduct(long productId, ProductUpdateDTO productUpdateDTO, String username) throws RuntimeException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not Found"));

        if(!product.getSeller().getUsername().equals(username)) {
            throw new RuntimeException("Not allowed to update product");
        }

        if(productUpdateDTO.getTitle() != null) product.setTitle(productUpdateDTO.getTitle());
        if(productUpdateDTO.getDescription() != null) product.setDescription(productUpdateDTO.getDescription());
        if(productUpdateDTO.getBasePrice() != null) product.setBasePrice(productUpdateDTO.getBasePrice());

        if(productUpdateDTO.getImageUrls()!=null){
            List<Image> updatedImages = productUpdateDTO.getImageUrls().stream().map(
                    url -> {
                        Image image = new Image();
                        image.setImageUrl(url);
                        image.setProduct(product);
                        return image;
                    }
            ).toList();

            product.getImages().clear();
            product.getImages().addAll(updatedImages);
        }

        Product updatedProduct = productRepository.save(product);
        return new ProductResponseDTO(updatedProduct);
    }

    public void deleteProduct(long productId, String username) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not Found"));

        if(!product.getSeller().getUsername().equals(username)) {
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
        List<Product> products = productRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
        return products.stream().map(ProductResponseDTO::new).collect(Collectors.toList());
    }

    public void addAuctionDetails(long productId, String username, AuctionScheduleRequestDTO auctionScheduleRequestDTO) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not Found"));

        Auction auction = new Auction();
        auction.setProduct(product);
        auction.setStartTime(auctionScheduleRequestDTO.getAuctionStartTime());
        auction.setEndTime(auctionScheduleRequestDTO.getAuctionEndTime());

        auctionRepository.save(auction);
    }
}
