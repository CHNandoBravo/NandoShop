package shop.nandoShop.nandoshop_app.services.interfaces;

import java.util.List;

public interface WebhookService {
    public void processWebhook(String platform, String payload);
}
