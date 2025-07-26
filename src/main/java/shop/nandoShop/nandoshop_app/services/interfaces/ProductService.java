package shop.nandoShop.nandoshop_app.services.interfaces;

import org.springframework.web.multipart.MultipartFile;
import shop.nandoShop.nandoshop_app.dtos.ProductResponseDTO;
import shop.nandoShop.nandoshop_app.dtos.requests.*;
import shop.nandoShop.nandoshop_app.entities.Product;

import java.util.List;
import java.util.function.Consumer;

public interface ProductService {
    Product create(ProductRequest productRequest);
    List<ProductResponseDTO> showAllMyProducts();
    List<ProductResponseDTO> showAllProducts();
    void streamAllProducts(Consumer<ProductResponseDTO> consumer);
    void streamProductsPaged(int offset, int limit, String category, Consumer<ProductResponseDTO> consumer);
    List<ProductResponseDTO> showRandom8Products();
    void deleteProduct(Long id);
    void updateStock(Long id, UpdateStockRequest request);
    void updatePrice(Long id, UpdatePriceProductRequest request);
    void updateName(Long id, UpdateNameProductRequest request);
    void updateImage(Long id, UpdateImageProductRequest request);
}
