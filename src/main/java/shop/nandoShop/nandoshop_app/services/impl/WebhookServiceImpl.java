package shop.nandoShop.nandoshop_app.services.impl;

import org.springframework.stereotype.Service;
import shop.nandoShop.nandoshop_app.services.interfaces.WebhookService;
import shop.nandoShop.nandoshop_app.strategies.interfaces.WebhookStrategy;

import java.util.Map;

@Service
public class WebhookServiceImpl implements WebhookService {

    private final Map<String, WebhookStrategy> strategies;

    public WebhookServiceImpl(Map<String, WebhookStrategy> strategies) {
        this.strategies = strategies;
    }

    public void processWebhook(String platform, String payload) {
        WebhookStrategy strategy = strategies.get(platform.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("No existe estrategia para la plataforma: " + platform);
        }
        strategy.handle(payload);
    }

}
