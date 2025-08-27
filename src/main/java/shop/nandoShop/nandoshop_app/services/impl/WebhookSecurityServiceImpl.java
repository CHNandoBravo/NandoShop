package shop.nandoShop.nandoshop_app.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shop.nandoShop.nandoshop_app.services.interfaces.WebhookSecurityService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class WebhookSecurityServiceImpl implements WebhookSecurityService {

    @Value("${mercado-pago.access-token}")
    private String mpSecret;

    @Override
    public boolean isValidSignature(String platform, String payload, String receivedSignature) {
        String secret = getSecretForPlatform(platform);
        if (secret == null) {
            throw new IllegalArgumentException("Plataforma desconocida: " + platform);
        }

        try {
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String calculatedSignature = Base64.getEncoder().encodeToString(hash);
            return calculatedSignature.equals(receivedSignature);
        } catch (Exception e) {
            return false;
        }

    }

    private String getSecretForPlatform(String platform) {
        return switch (platform.toLowerCase()) {
            case "mercadopago" -> mpSecret;
            default -> null;
        };
    }

}
