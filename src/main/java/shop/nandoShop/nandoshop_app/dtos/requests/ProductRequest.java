package shop.nandoShop.nandoshop_app.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String name;
    private String description;
    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal price;
    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;
    @NotNull(message = "La categoría es obligatoria")
    private Long categoryId;
}
