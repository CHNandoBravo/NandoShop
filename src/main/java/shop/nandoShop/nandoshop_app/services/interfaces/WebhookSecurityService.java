package shop.nandoShop.nandoshop_app.services.interfaces;

public interface WebhookSecurityService {
    public boolean isValidSignature(String platform, String payload, String receivedSignature);

}
