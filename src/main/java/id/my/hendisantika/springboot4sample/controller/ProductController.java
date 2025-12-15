package id.my.hendisantika.springboot4sample.controller;

import id.my.hendisantika.springboot4sample.dto.ApiResponse;
import id.my.hendisantika.springboot4sample.dto.PagedResponse;
import id.my.hendisantika.springboot4sample.dto.ProductRequest;
import id.my.hendisantika.springboot4sample.dto.ProductResponse;
import id.my.hendisantika.springboot4sample.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping(version = "1.0")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request) {
        log.info("REST request to create product: {}", request.getName());
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", response));
    }

    @GetMapping(path = "/{id}", version = "1.0")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        log.info("REST request to get product by ID: {}", id);
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping(version = "1.0")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        log.info("REST request to get all products");
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", products));
    }

    @GetMapping(path = "/paged", version = "1.0")
    public ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> getAllProductsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("REST request to get paged products - page: {}, size: {}", page, size);
        PagedResponse<ProductResponse> response = productService.getAllProductsPaged(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", response));
    }

    @PutMapping(path = "/{id}", version = "1.0")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        log.info("REST request to update product with ID: {}", id);
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", response));
    }

    @DeleteMapping(path = "/{id}", version = "1.0")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        log.info("REST request to delete product with ID: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }

    @GetMapping(path = "/category/{category}", version = "1.0")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(
            @PathVariable String category) {
        log.info("REST request to get products by category: {}", category);
        List<ProductResponse> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", products));
    }

    @GetMapping(path = "/active", version = "1.0")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getActiveProducts() {
        log.info("REST request to get active products");
        List<ProductResponse> products = productService.getActiveProducts();
        return ResponseEntity.ok(ApiResponse.success("Active products retrieved successfully", products));
    }

    @GetMapping(path = "/search", version = "1.0")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(
            @RequestParam String keyword) {
        log.info("REST request to search products with keyword: {}", keyword);
        List<ProductResponse> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", products));
    }
}
