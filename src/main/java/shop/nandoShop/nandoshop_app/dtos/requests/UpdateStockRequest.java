package shop.nandoShop.nandoshop_app.dtos.requests;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateStockRequest {
    @Min(value = 0, message = "El stock no puede ser negativo")
    private int newStock;
}
