package shop.nandoShop.nandoshop_app.services.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.nandoShop.nandoshop_app.dtos.AssetDTO;
import shop.nandoShop.nandoshop_app.dtos.ProductResponseDTO;
import shop.nandoShop.nandoshop_app.dtos.requests.*;
import shop.nandoShop.nandoshop_app.entities.Category;
import shop.nandoShop.nandoshop_app.entities.Product;
import shop.nandoShop.nandoshop_app.entities.User;
import shop.nandoShop.nandoshop_app.exceptions.NotFoundException;
import shop.nandoShop.nandoshop_app.exceptions.ProductNotFoundException;
import shop.nandoShop.nandoshop_app.mappers.ProductMapper;
import shop.nandoShop.nandoshop_app.repositories.CategoryRepository;
import shop.nandoShop.nandoshop_app.repositories.ProductRepository;
import shop.nandoShop.nandoshop_app.repositories.UserRepository;
import shop.nandoShop.nandoshop_app.services.interfaces.AssetsService;
import shop.nandoShop.nandoshop_app.services.interfaces.ProductService;

import java.util.List;
import java.util.function.Consumer;

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
    public void streamProductsPaged(int offset, int limit, String category, String query, Consumer<ProductResponseDTO> consumer) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        List<Product> products = productRepository.findPaged(category, query, pageable); // paginación custom aquí
        products.forEach(product -> {
            ProductResponseDTO dto = mapToDto(product);
            consumer.accept(dto);
        });
    }
    private ProductResponseDTO mapToDto(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getCategory().getName(),
                product.getName(),
                product.getImageUrl(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    @Override
    public List<ProductResponseDTO> showRandom8Products() {
        List<Product> products = productRepository.findRandom8Products();

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

    @Override
    public void updateImage(Long id, UpdateImageProductRequest request) {
        User user = userService.getCurrentUser();

        MDC.put("userId", String.valueOf(user.getId()));
        MDC.put("productId", String.valueOf(id));
        try {
            log.debug("Inicio actualizacion de la imagen del producto id: {} para usuario id: {}", id, user.getId());

            Product product = productRepository.findByIdAndSeller(id, user)
                    .orElseThrow(() -> {
                        log.warn("Producto no encontrado o no pertenece al usuario: productId={}, userId={}", id, user.getId());
                        return new NotFoundException("Producto con id: " + id + " no encontrado o no pertenece al usuario.");
                    });
            assetsService.deleteFile(product.getImageUrl());
            AssetDTO imageUrl = assetsService.uploadFile(request.getImage());
            product.setImageUrl(imageUrl.url());

            productRepository.save(product);
            log.info("Producto marcado como actualizado: id={}, nombre={}", product.getId(), product.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            MDC.remove("userId");
            MDC.remove("productId");
        }
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return ProductMapper.toResponse(product);
    }

    public Product getProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
