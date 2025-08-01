package shop.nandoShop.nandoshop_app.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.nandoShop.nandoshop_app.entities.Product;
import shop.nandoShop.nandoshop_app.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySeller(User seller);
    Optional<Product> findByIdAndSeller(Long id, User seller);
    @Query(value = "SELECT * FROM products ORDER BY RAND() LIMIT 8", nativeQuery = true)
    List<Product> findRandom8Products();
    @Query("SELECT p FROM Product p")
    Stream<Product> streamAll();

    @Query("""
    SELECT p FROM Product p
    WHERE (:category IS NULL OR p.category = :category)
    AND (:query IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')))
    """)
    List<Product> findPaged(@Param("category") String category, @Param("query") String query, Pageable pageable);
}
