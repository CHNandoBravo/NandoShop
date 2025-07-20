package shop.nandoShop.nandoshop_app.services.interfaces;

import org.springframework.web.multipart.MultipartFile;
import shop.nandoShop.nandoshop_app.dtos.ProductResponseDTO;
import shop.nandoShop.nandoshop_app.dtos.requests.ProductRequest;
import shop.nandoShop.nandoshop_app.dtos.requests.UpdateNameProductRequest;
import shop.nandoShop.nandoshop_app.dtos.requests.UpdatePriceProductRequest;
import shop.nandoShop.nandoshop_app.dtos.requests.UpdateStockRequest;
import shop.nandoShop.nandoshop_app.entities.Product;

import java.util.List;

public interface ProductService {
    Product create(ProductRequest productRequest);
    List<ProductResponseDTO> showAllMyProducts();
    void deleteProduct(Long id);
    void updateStock(Long id, UpdateStockRequest request);
    void updatePrice(Long id, UpdatePriceProductRequest request);
    void updateName(Long id, UpdateNameProductRequest request);
}
