package shop.nandoShop.nandoshop_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.nandoShop.nandoshop_app.entities.Product;
import shop.nandoShop.nandoshop_app.entities.User;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySeller(User seller);

}
