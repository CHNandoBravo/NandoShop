package shop.nandoShop.nandoshop_app.mappers;

import shop.nandoShop.nandoshop_app.dtos.ProductResponseDTO;
import shop.nandoShop.nandoshop_app.entities.Product;

public class ProductMapper {
    public static ProductResponseDTO toResponse(Product product) {
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

}
