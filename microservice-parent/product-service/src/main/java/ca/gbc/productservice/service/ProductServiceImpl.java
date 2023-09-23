package ca.gbc.productservice.service;
// importing the required packages

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.model.Product;
import ca.gbc.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

//import query
import org.springframework.data.mongodb.core.query.Query;
import java.util.List;

// @Service annotation is used to mark the class as a service provider
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    // construct injection
    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void createProduct(ProductRequest productRequest) {
        log.info("Creating a new product {}", productRequest.getName());

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        // save the product to the database
        productRepository.save(product);

        // log the product
        log.info("Product {} is saved", product.getId());
    }

    @Override
    public String updateProduct(String productId, ProductRequest productRequest) {

        log.info("Updating product with id {}", productId);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(productId));
        Product product = mongoTemplate.findOne(query, Product.class);

        if(product!=null) {
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());

            // save the product to the database

            // log the product
            log.info("Product {} is updated", product.getId());
            return productRepository.save(product).getId();

        }
        return productId.toString();



    }

    @Override
    public void deleteProduct(String productId) {

        log.info("Product {} is deleted", productId);
        productRepository.deleteById(productId);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        log.info("Retrieving all products");
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    // mapToProductResponse method
    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .Id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
