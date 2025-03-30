package shop.nandoShop.nandoshop_app.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.nandoShop.nandoshop_app.dtos.requests.ProductRequest;
import shop.nandoShop.nandoshop_app.entities.Category;
import shop.nandoShop.nandoshop_app.entities.Product;
import shop.nandoShop.nandoshop_app.entities.User;
import shop.nandoShop.nandoshop_app.repositories.CategoryRepository;
import shop.nandoShop.nandoshop_app.repositories.ProductRepository;
import shop.nandoShop.nandoshop_app.repositories.UserRepository;
import shop.nandoShop.nandoshop_app.services.interfaces.ProductService;

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
}
