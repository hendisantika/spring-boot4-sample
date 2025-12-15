package id.my.hendisantika.springboot4sample.controller;

import id.my.hendisantika.springboot4sample.dto.ProductRequest;
import id.my.hendisantika.springboot4sample.entity.Product;
import id.my.hendisantika.springboot4sample.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot4-sample
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 15/12/25
 * Time: 09.00
 * To change this template use File | Settings | File Templates.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ProductControllerIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.5.0")
            .withDatabaseName("spring_boot4_db")
            .withUsername("yu71")
            .withPassword("53cret");
    @LocalServerPort
    private int port;
    @Autowired
    private ProductRepository productRepository;
    private RestClient restClient;
    private String baseUrl;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/products";
        restClient = RestClient.create();
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a new product")
    void shouldCreateProduct() {
        ProductRequest request = ProductRequest.builder()
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .quantity(100)
                .category("Electronics")
                .isActive(true)
                .build();

        Map response = restClient.post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("success")).isEqualTo(true);
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        assertThat(data.get("name")).isEqualTo("Test Product");
    }

    @Test
    @DisplayName("Should get product by ID")
    void shouldGetProductById() {
        // First create a product directly in the repository
        Product product = Product.builder()
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .quantity(100)
                .category("Electronics")
                .isActive(true)
                .build();
        Product savedProduct = productRepository.save(product);

        // Then get the product by ID
        Map response = restClient.get()
                .uri(baseUrl + "/" + savedProduct.getId())
                .retrieve()
                .body(Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("success")).isEqualTo(true);
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        assertThat(data.get("name")).isEqualTo("Test Product");
    }

    @Test
    @DisplayName("Should get all products")
    void shouldGetAllProducts() {
        // Create two products directly in the repository
        Product product1 = Product.builder()
                .name("Product 1")
                .price(new BigDecimal("50.00"))
                .quantity(10)
                .isActive(true)
                .build();

        Product product2 = Product.builder()
                .name("Product 2")
                .price(new BigDecimal("75.00"))
                .quantity(20)
                .isActive(true)
                .build();

        productRepository.save(product1);
        productRepository.save(product2);

        // Get all products
        Map response = restClient.get()
                .uri(baseUrl)
                .retrieve()
                .body(Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("success")).isEqualTo(true);
        var products = (java.util.List<?>) response.get("data");
        assertThat(products).hasSize(2);
    }

    @Test
    @DisplayName("Should update a product")
    void shouldUpdateProduct() {
        // First create a product directly in the repository
        Product product = Product.builder()
                .name("Original Name")
                .price(new BigDecimal("50.00"))
                .quantity(10)
                .isActive(true)
                .build();
        Product savedProduct = productRepository.save(product);

        // Update the product
        ProductRequest updateRequest = ProductRequest.builder()
                .name("Updated Name")
                .price(new BigDecimal("75.00"))
                .quantity(20)
                .build();

        Map response = restClient.put()
                .uri(baseUrl + "/" + savedProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateRequest)
                .retrieve()
                .body(Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("success")).isEqualTo(true);
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        assertThat(data.get("name")).isEqualTo("Updated Name");
    }

    @Test
    @DisplayName("Should delete a product")
    void shouldDeleteProduct() {
        // First create a product directly in the repository
        Product product = Product.builder()
                .name("Product to Delete")
                .price(new BigDecimal("50.00"))
                .quantity(10)
                .isActive(true)
                .build();
        Product savedProduct = productRepository.save(product);

        // Delete the product
        Map deleteResponse = restClient.delete()
                .uri(baseUrl + "/" + savedProduct.getId())
                .retrieve()
                .body(Map.class);

        assertThat(deleteResponse).isNotNull();
        assertThat(deleteResponse.get("success")).isEqualTo(true);

        // Verify product is deleted
        assertThat(productRepository.findById(savedProduct.getId())).isEmpty();
    }

    @Test
    @DisplayName("Should search products by keyword")
    void shouldSearchProducts() {
        // Create products
        Product product1 = Product.builder()
                .name("Apple iPhone")
                .description("Latest smartphone")
                .price(new BigDecimal("999.00"))
                .quantity(50)
                .isActive(true)
                .build();

        Product product2 = Product.builder()
                .name("Samsung Galaxy")
                .description("Android smartphone")
                .price(new BigDecimal("899.00"))
                .quantity(30)
                .isActive(true)
                .build();

        productRepository.save(product1);
        productRepository.save(product2);

        // Search for smartphone
        Map response = restClient.get()
                .uri(baseUrl + "/search?keyword=smartphone")
                .retrieve()
                .body(Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("success")).isEqualTo(true);
        var products = (java.util.List<?>) response.get("data");
        assertThat(products).hasSize(2);
    }
}
