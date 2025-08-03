package shop.nandoShop.nandoshop_app.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    APPROVED("approved"),        // El pago fue aprobado
    IN_PROCESS("in_process"),    // El pago está en proceso de revisión
    PENDING("pending"),          // El usuario aún no completó el pago
    REJECTED("rejected"),        // El pago fue rechazado
    CANCELLED("cancelled"),      // El pago fue cancelado por el usuario o vendedor
    REFUNDED("refunded"),        // El pago fue devuelto al usuario
    CHARGED_BACK("charged_back"); // Hubo un contracargo del banco
    private final String value;
}
