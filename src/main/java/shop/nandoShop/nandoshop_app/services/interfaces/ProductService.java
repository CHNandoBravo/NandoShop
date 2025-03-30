package shop.nandoShop.nandoshop_app.services.interfaces;

import shop.nandoShop.nandoshop_app.dtos.requests.ProductRequest;
import shop.nandoShop.nandoshop_app.entities.Product;

public interface ProductService {
    Product create(ProductRequest productRequest);
}
