package shop.nandoShop.nandoshop_app.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
}
