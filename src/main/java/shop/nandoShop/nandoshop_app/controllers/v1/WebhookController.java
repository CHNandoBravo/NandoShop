package shop.nandoShop.nandoshop_app.controllers.v1;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.nandoShop.nandoshop_app.services.interfaces.WebhookService;

import java.nio.charset.StandardCharsets;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping("/{platform}")
    public ResponseEntity<Void> receiveWebhook(
            @PathVariable String platform,
            HttpServletRequest request
    ) {
        try {
            String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

            log.info("[WEBHOOK] Plataforma: {}, Payload recibido: {}", platform, body);

            webhookService.processWebhook(platform, body);

            return ResponseEntity.ok().build(); // Devolvemos rápido para que no reintente la pasarela

        } catch (Exception e) {
            log.error("Error al procesar webhook para plataforma: {}", platform, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
