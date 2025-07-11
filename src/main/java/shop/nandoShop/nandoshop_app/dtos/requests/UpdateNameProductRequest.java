package shop.nandoShop.nandoshop_app.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateNameProductRequest {
    @NotEmpty
    @Size(min=10, max=355, message = "El nombre debe tener entre 10 y 355 caracteres")
    private String newName;
}
