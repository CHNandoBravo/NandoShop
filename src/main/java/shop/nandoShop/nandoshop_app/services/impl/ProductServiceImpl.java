package shop.nandoShop.nandoshop_app.services.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import shop.nandoShop.nandoshop_app.dtos.AssetDTO;
import shop.nandoShop.nandoshop_app.dtos.ProductResponseDTO;
import shop.nandoShop.nandoshop_app.dtos.requests.ProductRequest;
import shop.nandoShop.nandoshop_app.dtos.requests.UpdateNameProductRequest;
import shop.nandoShop.nandoshop_app.dtos.requests.UpdatePriceProductRequest;
import shop.nandoShop.nandoshop_app.dtos.requests.UpdateStockRequest;
import shop.nandoShop.nandoshop_app.entities.Category;
import shop.nandoShop.nandoshop_app.entities.Product;
import shop.nandoShop.nandoshop_app.entities.User;
import shop.nandoShop.nandoshop_app.exceptions.NotFoundException;
import shop.nandoShop.nandoshop_app.repositories.CategoryRepository;
import shop.nandoShop.nandoshop_app.repositories.ProductRepository;
import shop.nandoShop.nandoshop_app.repositories.UserRepository;
import shop.nandoShop.nandoshop_app.services.interfaces.AssetsService;
import shop.nandoShop.nandoshop_app.services.interfaces.ProductService;

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

    @Autowired
    AssetsService assetsService;

    @Override
    public Product create(ProductRequest productRequest) {
        try {
            User user = userService.getCurrentUser();

            Category category = categoryRepository.getById(productRequest.getCategoryId());

            AssetDTO imageUrl = assetsService.uploadFile(productRequest.getImage());

            Product product = new Product();
            product.setCategory(category);
            product.setName(productRequest.getName());
            product.setSeller(user);
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setStock(productRequest.getStock());
            product.setImageUrl(imageUrl.url());
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
                        product.getImageUrl(),
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

    @Override
    public void updatePrice(Long id, UpdatePriceProductRequest request) {
        User user = userService.getCurrentUser();

        MDC.put("userId", String.valueOf(user.getId()));
        MDC.put("productId", String.valueOf(id));
        try {
            log.debug("Inicio actualizacion del precio del producto id: {} para usuario id: {}", id, user.getId());

            Product product = productRepository.findByIdAndSeller(id, user)
                    .orElseThrow(() -> {
                        log.warn("Producto no encontrado o no pertenece al usuario: productId={}, userId={}", id, user.getId());
                        return new NotFoundException("Producto con id: " + id + " no encontrado o no pertenece al usuario.");
                    });
            product.setPrice(request.getNewPrice());

            productRepository.save(product);
            log.info("Producto marcado como actualizado: id={}, nombre={}", product.getId(), product.getName());
        } finally {
            MDC.remove("userId");
            MDC.remove("productId");
        }
    }

    @Override
    public void updateName(Long id, UpdateNameProductRequest request) {
        User user = userService.getCurrentUser();

        MDC.put("userId", String.valueOf(user.getId()));
        MDC.put("productId", String.valueOf(id));
        try {
            log.debug("Inicio actualizacion del nombre del producto id: {} para usuario id: {}", id, user.getId());

            Product product = productRepository.findByIdAndSeller(id, user)
                    .orElseThrow(() -> {
                        log.warn("Producto no encontrado o no pertenece al usuario: productId={}, userId={}", id, user.getId());
                        return new NotFoundException("Producto con id: " + id + " no encontrado o no pertenece al usuario.");
                    });
            product.setName(request.getNewName());

            productRepository.save(product);
            log.info("Producto marcado como actualizado: id={}, nombre={}", product.getId(), product.getName());
        } finally {
            MDC.remove("userId");
            MDC.remove("productId");
        }
    }
}
