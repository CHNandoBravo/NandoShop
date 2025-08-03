package shop.nandoShop.nandoshop_app.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentPreferenceResponse {
    private String preferenceId;
    private String initPoint;
}
