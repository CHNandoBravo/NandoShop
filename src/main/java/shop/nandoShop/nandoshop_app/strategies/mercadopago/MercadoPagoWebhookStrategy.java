package shop.nandoShop.nandoshop_app.strategies.mercadopago;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shop.nandoShop.nandoshop_app.dtos.responses.MercadoPagoWebhookResponse;
import shop.nandoShop.nandoshop_app.services.interfaces.PaymentService;
import shop.nandoShop.nandoshop_app.strategies.interfaces.WebhookStrategy;

@Slf4j
@Service("mercadopago")
public class MercadoPagoWebhookStrategy implements WebhookStrategy {

    private final ObjectMapper objectMapper;

    private final PaymentClient paymentClient;

    @Autowired
    public MercadoPagoWebhookStrategy(
            ObjectMapper objectMapper,
            @Value("${mercado-pago.access-token}") String mpAccessToken) {
        this.objectMapper = objectMapper;
        MercadoPagoConfig.setAccessToken(mpAccessToken);
        this.paymentClient = new PaymentClient();
    }

    @Autowired
    PaymentService paymentService;

    @Override
    public void handle(String payload) {
        try{
            MercadoPagoWebhookResponse response = objectMapper.readValue(payload, MercadoPagoWebhookResponse.class);
            System.out.println("Procesando MercadoPago: " + payload);
            switch (response.getAction()) {
                case "payment.created":
                    Long paymentId = Long.parseLong(response.getData().getId());
                    processPayment(paymentId);
                    break;
                default:
                    log.info("Evento ignorado: {}", response.getAction());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processPayment(Long paymentId) {
        try {
            Payment mpPayment = paymentClient.get(paymentId);
            log.info("Estado: {}", mpPayment.getStatus());
            log.info("Monto: {}", mpPayment.getTransactionAmount());

            Long productPaymentId = Long.parseLong(mpPayment.getAdditionalInfo().getItems().get(0).getId());

            paymentService.approvePayment(productPaymentId);

        } catch (MPApiException e) {
            log.error("Error API de Mercado Pago: {}", e.getApiResponse().getContent());
        } catch (MPException e) {
            log.error("Error interno SDK de Mercado Pago", e);
        }
    }
}
