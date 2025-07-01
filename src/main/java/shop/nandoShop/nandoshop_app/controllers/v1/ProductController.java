package shop.nandoShop.nandoshop_app.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.nandoShop.nandoshop_app.dtos.ProductResponseDTO;
import shop.nandoShop.nandoshop_app.dtos.requests.ProductRequest;
import shop.nandoShop.nandoshop_app.entities.Product;
import shop.nandoShop.nandoshop_app.services.interfaces.ProductService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/v1")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.create(productRequest));
    }

    @GetMapping("/my_products")
    public ResponseEntity<List<ProductResponseDTO>> showProductsPaginated() {
        return ResponseEntity.ok(productService.showAllMyProducts());
    }
}
