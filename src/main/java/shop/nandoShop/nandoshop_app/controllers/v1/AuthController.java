package shop.nandoShop.nandoshop_app.controllers.v1;

import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.nandoShop.nandoshop_app.Providers.JwtTokenProvider;
import shop.nandoShop.nandoshop_app.dtos.requests.LoginRequest;
import shop.nandoShop.nandoshop_app.dtos.requests.RegisterRequest;
import shop.nandoShop.nandoshop_app.dtos.responses.JwtAuthenticationResponse;
import shop.nandoShop.nandoshop_app.dtos.responses.UserResponse;
import shop.nandoShop.nandoshop_app.entities.User;
import shop.nandoShop.nandoshop_app.enums.Role;
import shop.nandoShop.nandoshop_app.services.impl.UserServiceImpl;

@Validated
@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserServiceImpl userService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.registerUser(registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getFirstName(), registerRequest.getLastName(), Role.USER);
            return ResponseEntity.ok(user);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        try {
            // Se autentica usando email y contraseña
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            // Obtenemos el usuario autenticado
            User user = (User) authentication.getPrincipal();

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenProvider.generateToken(user);

            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try{
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
            }

            // Se obtiene la entidad User correctamente
            User user = (User) authentication.getPrincipal();

            // Crear una respuesta personalizada sin exponer la contraseña
            UserResponse userResponse = new UserResponse(user.getEmail(), user.getRole().name());

            return ResponseEntity.ok(userResponse);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo obtener el usuario");
        }

    }
}
