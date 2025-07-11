package shop.nandoShop.nandoshop_app.services.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import shop.nandoShop.nandoshop_app.dtos.ProductResponseDTO;
import shop.nandoShop.nandoshop_app.dtos.requests.ProductRequest;
import shop.nandoShop.nandoshop_app.dtos.requests.UpdateStockRequest;
import shop.nandoShop.nandoshop_app.dtos.responses.ApiResponse;
import shop.nandoShop.nandoshop_app.entities.Category;
import shop.nandoShop.nandoshop_app.entities.Product;
import shop.nandoShop.nandoshop_app.entities.User;
import shop.nandoShop.nandoshop_app.exceptions.NotFoundException;
import shop.nandoShop.nandoshop_app.repositories.CategoryRepository;
import shop.nandoShop.nandoshop_app.repositories.ProductRepository;
import shop.nandoShop.nandoshop_app.repositories.UserRepository;
import shop.nandoShop.nandoshop_app.services.interfaces.ProductService;
import shop.nandoShop.nandoshop_app.utils.AuthUtil;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceImpl userService;

    @Override
    public Product create(ProductRequest productRequest) {
        try {
            User user = userService.getCurrentUser();

            Category category = categoryRepository.getById(productRequest.getCategoryId());

            Product product = new Product();
            product.setCategory(category);
            product.setName(productRequest.getName());
            product.setSeller(user);
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setStock(productRequest.getStock());

            productRepository.save(product);
            return product;
        }
        catch (Exception e) {
            throw new RuntimeException("Error al crear la entidad Product: "+e.getMessage(), e);
        }
    }

    @Override
    public List<ProductResponseDTO> showAllMyProducts() {
        User user = userService.getCurrentUser();
        List<Product> products = productRepository.findBySeller(user);

        return products.stream()
                .map(product -> new ProductResponseDTO(
                        product.getId(),
                        product.getCategory().getName(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStock(),
                        product.getCreatedAt(),
                        product.getUpdatedAt()
                ))
                .toList();
    }
    @Transactional
    @Override
    public void deleteProduct(Long id) {
        User user = userService.getCurrentUser();

        MDC.put("userId", String.valueOf(user.getId()));
        MDC.put("productId", String.valueOf(id));

        try {
            log.debug("Inicio eliminación producto id: {} para usuario id: {}", id, user.getId());

            Product product = productRepository.findByIdAndSeller(id, user)
                    .orElseThrow(() -> {
                        log.warn("Producto no encontrado o no pertenece al usuario: productId={}, userId={}", id, user.getId());
                        return new NotFoundException("Producto con id: " + id + " no encontrado o no pertenece al usuario.");
                    });

            productRepository.delete(product);

            log.info("Producto marcado como eliminado: id={}, nombre={}", product.getId(), product.getName());

        } finally {
            MDC.remove("userId");
            MDC.remove("productId");
        }
    }

    @Override
    public void updateStock(Long id, UpdateStockRequest request) {
        User user = userService.getCurrentUser();

        MDC.put("userId", String.valueOf(user.getId()));
        MDC.put("productId", String.valueOf(id));
        try {
            log.debug("Inicio actualizacion del stock del producto id: {} para usuario id: {}", id, user.getId());

            Product product = productRepository.findByIdAndSeller(id, user)
                    .orElseThrow(() -> {
                        log.warn("Producto no encontrado o no pertenece al usuario: productId={}, userId={}", id, user.getId());
                        return new NotFoundException("Producto con id: " + id + " no encontrado o no pertenece al usuario.");
                    });
            product.setStock(request.getNewStock());

            productRepository.save(product);
            log.info("Producto marcado como actualizado: id={}, nombre={}", product.getId(), product.getName());
        } finally {
            MDC.remove("userId");
            MDC.remove("productId");
        }
    }
}
