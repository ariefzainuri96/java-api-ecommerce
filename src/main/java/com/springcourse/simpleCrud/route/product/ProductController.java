package com.springcourse.simpleCrud.route.product;

import com.springcourse.simpleCrud.model.Response.BaseResponse;
import com.springcourse.simpleCrud.model.schema.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<BaseResponse<List<Product>>> getProducts(@RequestParam(required = false) String name) {
        if (name == null) {
            return productService.getAllProduct();
        } else {
            return productService.searchProduct(name);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Product>> getProductById(
            @PathVariable int id) {
        return productService.getProductById(id);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse<Product>> addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse<Product>> patchProduct(@PathVariable int id,
            @RequestBody Map<String, Object> updates) {
        return productService.updateProduct(id, updates);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse<Product>> deleteProduct(@PathVariable int id) {
        return productService.deleteProduct(id);
    }
}
