package shop.nandoShop.nandoshop_app.mappers;

import shop.nandoShop.nandoshop_app.dtos.PreferenceDTO;
import shop.nandoShop.nandoshop_app.entities.Payment;
import shop.nandoShop.nandoshop_app.entities.Product;
import shop.nandoShop.nandoshop_app.enums.Currency;

public class PaymentMapper {
    public static PreferenceDTO mapToPreferenceDTO(Payment payment, Product product) {
        PreferenceDTO dto = new PreferenceDTO();
        dto.setId(String.valueOf(payment.getId()));
        dto.setTitle(product.getName());
        dto.setCurrency(Currency.ARS);
        dto.setQuantity(payment.getQuantity());
        dto.setPrice(payment.getUnitPrice());
        return dto;
    }

}
