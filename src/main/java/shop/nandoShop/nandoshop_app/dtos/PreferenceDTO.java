package shop.nandoShop.nandoshop_app.dtos;

import lombok.Data;
import shop.nandoShop.nandoshop_app.enums.Currency;

import java.math.BigDecimal;

@Data
public class PreferenceDTO {
    private String id;
    private String title;
    private Currency currency;
    private int quantity;
    private BigDecimal price;
}
