package shop.nandoShop.nandoshop_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.nandoShop.nandoshop_app.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
