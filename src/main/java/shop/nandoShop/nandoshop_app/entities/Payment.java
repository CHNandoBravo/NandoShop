package shop.nandoShop.nandoshop_app.entities;

import jakarta.persistence.*;
import lombok.Data;
import shop.nandoShop.nandoshop_app.enums.PaymentStatus;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentId;

    @ManyToOne
    @JoinColumn(name = "payer_id", nullable = true)
    private User payer;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    private int quantity;
}
