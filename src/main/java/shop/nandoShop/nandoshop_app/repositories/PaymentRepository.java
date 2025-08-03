package shop.nandoShop.nandoshop_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.nandoShop.nandoshop_app.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
