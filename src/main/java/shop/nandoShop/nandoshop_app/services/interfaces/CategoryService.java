package shop.nandoShop.nandoshop_app.services.interfaces;

import shop.nandoShop.nandoshop_app.dtos.requests.CategoryRequest;
import shop.nandoShop.nandoshop_app.entities.Category;

public interface CategoryService {
    Category create(CategoryRequest categoryRequest);
}
