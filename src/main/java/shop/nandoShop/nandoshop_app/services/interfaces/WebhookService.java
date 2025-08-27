package shop.nandoShop.nandoshop_app.services.interfaces;

public interface WebhookService {
    public void processWebhook(String platform, String payload);
}
