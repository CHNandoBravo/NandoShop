package shop.nandoShop.nandoshop_app.services.impl;

import org.springframework.stereotype.Service;
import shop.nandoShop.nandoshop_app.services.interfaces.WebhookService;
import shop.nandoShop.nandoshop_app.strategies.interfaces.WebhookStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebhookServiceImpl implements WebhookService {

    private final Map<String, WebhookStrategy> strategies;

    public WebhookServiceImpl(List<WebhookStrategy> strategyList) {
        this.strategies = new HashMap<>();
        for (WebhookStrategy strategy : strategyList) {
            String name = strategy.getClass().getAnnotation(Service.class).value(); // usa el nombre del @Service("x")
            this.strategies.put(name.toLowerCase(), strategy);
        }
    }

    public void processWebhook(String platform, String payload) {
        WebhookStrategy strategy = strategies.get(platform.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("No existe estrategia para la plataforma: " + platform);
        }
        strategy.handle(payload);
    }

}
