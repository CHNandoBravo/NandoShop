package shop.nandoShop.nandoshop_app.controllers.v1;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import shop.nandoShop.nandoshop_app.dtos.ProductResponseDTO;
import shop.nandoShop.nandoshop_app.dtos.requests.ProductRequest;
import shop.nandoShop.nandoshop_app.dtos.requests.UpdateStockRequest;
import shop.nandoShop.nandoshop_app.dtos.responses.ApiResponse;
import shop.nandoShop.nandoshop_app.entities.Product;
import shop.nandoShop.nandoshop_app.exceptions.NotFoundException;
import shop.nandoShop.nandoshop_app.services.interfaces.ProductService;

import java.util.List;

@Slf4j
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

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@Positive @PathVariable Long id){
        log.info("Request DELETE /products/{} iniciada", id);
        try {
            productService.deleteProduct(id);
            log.info("Request DELETE /products/{} procesada con éxito", id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            log.warn("Eliminación fallida - producto no encontrado: id={}", id);
            throw ex;
        } catch (AccessDeniedException ex) {
            log.warn("Eliminación fallida - acceso denegado para producto id={}", id);
            throw ex;
        } catch (Exception ex) {
            log.error("Error inesperado eliminando producto id={}", id, ex);
            throw ex;
        }
    }
    @PutMapping("/product/stock/{id}")
    public ResponseEntity<Void> updateStock(@Positive @PathVariable Long id, @Valid @RequestBody UpdateStockRequest request){
        log.info("Request UPDATE /products/{} iniciada", id);
        try {
            productService.updateStock(id, request);
            log.info("Request UPDATE /products/{} procesada con éxito", id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            log.warn("Actualizacion fallida - producto no encontrado: id={}", id);
            throw ex;
        } catch (AccessDeniedException ex) {
            log.warn("Actualizacion fallida - acceso denegado para producto id={}", id);
            throw ex;
        } catch (Exception ex) {
            log.error("Error inesperado eliminando producto id={}", id, ex);
            throw ex;
        }
    }
}
