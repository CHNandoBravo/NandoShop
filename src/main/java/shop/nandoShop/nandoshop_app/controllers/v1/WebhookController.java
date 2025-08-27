package shop.nandoShop.nandoshop_app.controllers.v1;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.nandoShop.nandoshop_app.services.interfaces.WebhookSecurityService;
import shop.nandoShop.nandoshop_app.services.interfaces.WebhookService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    private final WebhookSecurityService webhookSecurityService;

    @PostMapping("/{platform}")
    public ResponseEntity<Void> receiveWebhook(
            @PathVariable String platform,
            @RequestHeader("x-signature") String signature,
            @RequestBody String body
            ) {
        try {
            if (!webhookSecurityService.isValidSignature(platform, body, signature)) {
                log.warn("Firma inválida para plataforma {}", platform);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            log.info("[WEBHOOK] Plataforma: {}, Payload recibido: {}", platform, body);

            webhookService.processWebhook(platform, body);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("Error al procesar webhook para plataforma: {}", platform, e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
