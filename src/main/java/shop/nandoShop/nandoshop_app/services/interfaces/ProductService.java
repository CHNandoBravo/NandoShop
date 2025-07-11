package shop.nandoShop.nandoshop_app.services.interfaces;

import shop.nandoShop.nandoshop_app.dtos.ProductResponseDTO;
import shop.nandoShop.nandoshop_app.dtos.requests.ProductRequest;
import shop.nandoShop.nandoshop_app.dtos.requests.UpdateStockRequest;
import shop.nandoShop.nandoshop_app.entities.Product;

import java.util.List;

public interface ProductService {
    Product create(ProductRequest productRequest);
    List<ProductResponseDTO> showAllMyProducts();
    void deleteProduct(Long id);
    void updateStock(Long id, UpdateStockRequest request);
}
