package shop.nandoShop.nandoshop_app.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.nandoShop.nandoshop_app.dtos.requests.CategoryRequest;
import shop.nandoShop.nandoshop_app.entities.Category;
import shop.nandoShop.nandoshop_app.repositories.CategoryRepository;
import shop.nandoShop.nandoshop_app.services.interfaces.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category create(CategoryRequest categoryRequest) {
        try {
            Category category = new Category();
            category.setName(categoryRequest.getName());

            categoryRepository.save(category);
            return category;
        }
        catch (Exception e) {
            throw new RuntimeException("Error al crear la entidad Category: "+e.getMessage(), e);
        }
    }
}
