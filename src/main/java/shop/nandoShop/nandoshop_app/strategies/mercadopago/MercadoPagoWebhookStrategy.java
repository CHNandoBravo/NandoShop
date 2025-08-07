package shop.nandoShop.nandoshop_app.strategies.mercadopago;

import org.springframework.stereotype.Service;
import shop.nandoShop.nandoshop_app.strategies.interfaces.WebhookStrategy;

@Service("mercadopago")
public class MercadoPagoWebhookStrategy implements WebhookStrategy {
    @Override
    public void handle(String payload) {
        // Aquí parseas el JSON y haces el trabajo real
        System.out.println("Procesando MercadoPago: " + payload);
    }
}
