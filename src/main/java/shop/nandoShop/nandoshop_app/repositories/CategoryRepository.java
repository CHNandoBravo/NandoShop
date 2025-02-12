package shop.nandoShop.nandoshop_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.nandoShop.nandoshop_app.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
