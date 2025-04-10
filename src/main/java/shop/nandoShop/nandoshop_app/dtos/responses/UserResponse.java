package shop.nandoShop.nandoshop_app.dtos.responses;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private String email;
    private String role;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;
}
