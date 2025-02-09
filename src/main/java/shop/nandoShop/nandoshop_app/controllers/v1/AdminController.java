package shop.nandoShop.nandoshop_app.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.nandoShop.nandoshop_app.dtos.requests.ChangeUserRoleRequest;
import shop.nandoShop.nandoshop_app.services.impl.UserServiceImpl;

@Validated
@RestController
@RequestMapping("/v1/admin")
public class AdminController {

    @Autowired
    UserServiceImpl userService;

    @PostMapping("/grant-seller-role")
    public ResponseEntity<?> grantSellerRole(@Valid @RequestBody ChangeUserRoleRequest request, Authentication authentication) {
        try {
            // Verificar si el usuario autenticado es ADMIN
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("No autenticado");
            }

            // Cambiar el rol del usuario objetivo a SELLER
            userService.grantSellerRole(request.getUserId());

            return ResponseEntity.ok("Rol de vendedor otorgado con éxito");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al cambiar el rol del usuario");
        }
    }
}
