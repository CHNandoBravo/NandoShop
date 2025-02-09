package shop.nandoShop.nandoshop_app.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import shop.nandoShop.nandoshop_app.Providers.JwtTokenProvider;
import shop.nandoShop.nandoshop_app.dtos.requests.LoginRequest;
import shop.nandoShop.nandoshop_app.dtos.requests.RegisterRequest;
import shop.nandoShop.nandoshop_app.dtos.responses.JwtAuthenticationResponse;
import shop.nandoShop.nandoshop_app.entities.User;
import shop.nandoShop.nandoshop_app.enums.Role;
import shop.nandoShop.nandoshop_app.services.impl.UserServiceImpl;

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
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // Se asigna siempre el rol USER, ignorando cualquier dato enviado en el request
            User user = userService.registerUser(registerRequest.getEmail(), registerRequest.getPassword(), Role.USER);
            return ResponseEntity.ok(user);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        try {
            // Se autentica usando email y contraseña
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenProvider.generateToken(loginRequest.getEmail());

            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
        }
        // Se retorna el principal (en este caso, los detalles del usuario)
        return ResponseEntity.ok(authentication.getPrincipal());
    }
}
