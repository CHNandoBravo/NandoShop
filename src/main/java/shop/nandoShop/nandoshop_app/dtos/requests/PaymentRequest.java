package shop.nandoShop.nandoshop_app.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class PaymentRequest {
    @NotNull(message = "El ID del producto no puede ser nulo")
    private Long idProduct;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer quantity;
}