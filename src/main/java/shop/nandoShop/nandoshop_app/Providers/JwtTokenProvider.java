package shop.nandoShop.nandoshop_app.Providers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtTokenProvider {
    // Cadena secreta (en producción, almacénala de forma segura)
    private String jwtSecret = "MIICWwIBAAKBgQCtYo32glu50D5XU3e7jk5RW3OvmmSIA4RxvS2N7acGixkvRkkn2yIGQXGhMmWzU6dT4gKEdsdJugP7iv5ornIs+VNLzunXLGu+qTHeG5BQJneugeTdSNZsu4cf7j0XrHo0mDONExkvKiXsG32XqqZ/8QJx9WZA3U+QV4JHrHd82wIDAQAB";

    @Autowired
    UserDetailsService userDetailsService;

    // Tiempo de expiración en milisegundos (ejemplo: 24 horas)
    private final long jwtExpirationInMs = 86400000;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    // Método para generar un token (simplificado)
    public String generateToken(String username) {
//            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            long nowMillis = System.currentTimeMillis();
            long expMillis = nowMillis + jwtExpirationInMs;
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new java.util.Date(nowMillis))
                    .setExpiration(new java.util.Date(expMillis))
                    .signWith(getSigningKey()) // Usa la llave generada
                    .compact();
    }

    // Método para extraer el username (subject) del token
    public String getUsernameFromJWT(String token) {
        try{
//            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())  // Se asigna la llave
                .build()             // Se construye el JwtParser
                .parseClaimsJws(token)  // Se parsea el token
                .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            System.err.println("Error al obtener el username del token: " + e.getMessage());
            return null;
        }
    }

    // Método para validar el token
    public boolean validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            // Aquí podrías capturar y loguear SignatureException, ExpiredJwtException, etc.
            System.out.println("Token JWT no válido: " + ex.getMessage());
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        String username = getUsernameFromJWT(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
