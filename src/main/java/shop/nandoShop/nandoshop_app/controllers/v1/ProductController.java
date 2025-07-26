package shop.nandoShop.nandoshop_app.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import shop.nandoShop.nandoshop_app.dtos.ProductResponseDTO;
import shop.nandoShop.nandoshop_app.dtos.requests.*;
import shop.nandoShop.nandoshop_app.dtos.responses.ApiResponse;
import shop.nandoShop.nandoshop_app.entities.Product;
import shop.nandoShop.nandoshop_app.exceptions.NotFoundException;
import shop.nandoShop.nandoshop_app.mappers.ProductMapper;
import shop.nandoShop.nandoshop_app.services.interfaces.ProductService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/product",consumes = "multipart/form-data")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @ModelAttribute ProductRequest productRequest) {
        Product product = productService.create(productRequest);
        ProductResponseDTO dto = ProductMapper.toResponse(product);
        return ResponseEntity.ok(dto);

    }

    @GetMapping("/my_products")
    public ResponseEntity<List<ProductResponseDTO>> showProductsPaginated() {
        return ResponseEntity.ok(productService.showAllMyProducts());
    }

    @GetMapping(value = "/products/all/sdfsdfsd", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponseDTO>> showAllProducts() {
        try {
            List<ProductResponseDTO> products = productService.showAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            // Podés usar un logger como log.error(...) si tenés logback configurado
            System.err.println("Error al obtener productos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build(); // HTTP 500
        }
    }
    @GetMapping(value = "/products/all", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public ResponseBodyEmitter streamAllProductsNdjson(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String category
    ) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                productService.streamProductsPaged(offset, limit, category, productDto -> {
                    try {
                        String json = objectMapper.writeValueAsString(productDto);
                        emitter.send(json + "\n", MediaType.APPLICATION_NDJSON);
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                });
                emitter.complete(); // se completa cuando ya se enviaron todos los productos de esa página
            } catch (Exception e) {
                emitter.completeWithError(e);
            } finally {
                executor.shutdown();
            }
        });
            
        return emitter;
    }

    @GetMapping("/products/8")
    public ResponseEntity<List<ProductResponseDTO>> showRandom8Products() {
        try {
            List<ProductResponseDTO> products = productService.showRandom8Products();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            // Podés usar un logger como log.error(...) si tenés logback configurado
            System.err.println("Error al obtener productos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build(); // HTTP 500
        }
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
    @PutMapping("/product/price/{id}")
    public ResponseEntity<Void> updatePrice(@Positive @PathVariable Long id, @Valid @RequestBody UpdatePriceProductRequest request){
        log.info("Request UPDATE /products/{} iniciada", id);
        try {
            productService.updatePrice(id, request);
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
    @PutMapping("/product/name/{id}")
    public ResponseEntity<Void> updateName(@Positive @PathVariable Long id, @Valid @RequestBody UpdateNameProductRequest request){
        log.info("Request UPDATE /products/{} iniciada", id);
        try {
            productService.updateName(id, request);
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
    @PutMapping("/product/image/{id}")
    public ResponseEntity<Void> updateImage(@Positive @PathVariable Long id, @ModelAttribute UpdateImageProductRequest request){
        log.info("Request UPDATE /products/{} iniciada", id);
        try {
            productService.updateImage(id, request);
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
