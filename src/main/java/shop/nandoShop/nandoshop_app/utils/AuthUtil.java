package shop.nandoShop.nandoshop_app.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import shop.nandoShop.nandoshop_app.entities.User;

public class AuthUtil {
    public static User getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return null; // O podés lanzar una excepción si preferís manejarlo afuera
            }

            return (User) authentication.getPrincipal();
        } catch (ClassCastException e) {
            // Puede pasar si el principal no es de tipo User
            System.err.println("Error al convertir el principal en User: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error al obtener el usuario autenticado: " + e.getMessage());
            return null;
        }
    }

}
