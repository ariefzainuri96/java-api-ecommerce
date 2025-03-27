package com.springcourse.simpleCrud.route.product;

import com.springcourse.simpleCrud.model.Response.BaseResponse;
import com.springcourse.simpleCrud.model.schema.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ResponseEntity<BaseResponse<List<Product>>> getAllProduct() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponse<>("Sukses", productRepository.findAll()));
    }

    public ResponseEntity<BaseResponse<Product>> getProductById(int id) {
        var product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse<>("Not found", product));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(
                String.format("%s", "Sukses mendapatkan barang"),
                product));
    }

    public ResponseEntity<BaseResponse<Product>> addProduct(Product product) {
        productRepository.save(product);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse<>(
                "Berhasil menambahkan produk", product));
    }

    public ResponseEntity<BaseResponse<Product>> updateProduct(int productId, Map<String, Object> updates) {
        var product = productRepository.findById(productId)
                .orElse(null);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse<>("Product not found", null));
        }

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Product.class, key);

            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, product, value);
            } else {
                throw new RuntimeException(String.format("Field %s not found", key));
            }
        });

        productRepository.save(product);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>("Product updated successfully", product));
    }

    public ResponseEntity<BaseResponse<Product>> deleteProduct(int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);

            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>("Produk berhasil dihapus", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse<>("Produk tidak ditemukan", null));
        }
    }

    public ResponseEntity<BaseResponse<List<Product>>> searchProduct(String keyword) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponse<>("Berhasil mencari produk", productRepository.searchProducts(keyword)));
    }
}
