package shop.nandoShop.nandoshop_app.services.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shop.nandoShop.nandoshop_app.Providers.JwtTokenProvider;
import shop.nandoShop.nandoshop_app.entities.User;
import shop.nandoShop.nandoshop_app.enums.Role;
import shop.nandoShop.nandoshop_app.repositories.UserRepository;
import shop.nandoShop.nandoshop_app.services.interfaces.GoogleAuthService;

import java.util.Collections;
import java.util.Optional;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    @Value("${google.client.id}")
    private String googleClientId;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;
    public String authenticateWithGoogle(String token) throws Exception {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token de Google es requerido");
        }

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance()
        ).setAudience(Collections.singletonList(googleClientId)).build();

        GoogleIdToken idToken = verifier.verify(token);
        if (idToken == null) {
            throw new SecurityException("Token de Google inválido");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String firstName = (String) payload.get("given_name");
        String lastName = (String) payload.get("family_name");
        // Podés buscar en la DB acá si querés
        // 🔍 Verificás si ya existe
        Optional<User> existingUser = userRepository.findByEmail(email);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get(); // ya está registrado
        } else {
            user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRole(Role.USER);
            user.setPassword(null); // si estás permitiendo null, sino podrías poner un hash dummy

            user = userRepository.save(user); // se registra si no existía
        }

        return tokenProvider.generateToken(user);
    }

}
