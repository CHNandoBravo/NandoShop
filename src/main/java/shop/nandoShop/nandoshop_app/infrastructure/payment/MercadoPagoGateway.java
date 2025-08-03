package shop.nandoShop.nandoshop_app.infrastructure.payment;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shop.nandoShop.nandoshop_app.dtos.PreferenceDTO;
import shop.nandoShop.nandoshop_app.dtos.responses.PaymentPreferenceResponse;
import shop.nandoShop.nandoshop_app.dtos.responses.PaymentStatusResponse;
import shop.nandoShop.nandoshop_app.exceptions.PaymentGatewayException;
import shop.nandoShop.nandoshop_app.repositories.ProductRepository;
import shop.nandoShop.nandoshop_app.services.interfaces.PaymentGateway;

import java.util.List;

@Slf4j
@Service
public class MercadoPagoGateway implements PaymentGateway {
    @Value("${mercado-pago.access-token}")
    private String accessToken;

    @Autowired
    ProductRepository productRepository;

    @Value("${site}")
    private String site;

    @Override
    public PaymentPreferenceResponse createPreference(PreferenceDTO dto) {
        try {
            MercadoPagoConfig.setAccessToken(accessToken);

            PreferenceClient client = new PreferenceClient();

            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id(dto.getId())
                    .title("Compra MP: " + dto.getTitle())
                    .quantity(dto.getQuantity())
                    .unitPrice(dto.getPrice())
                    .currencyId(dto.getCurrency().name())
                    .build();

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(site+"/payment/success")
                    .pending(site+"/payment/pending")
                    .failure(site+"/payment/failure")
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(List.of(item))
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .build();

            Preference preference = client.create(preferenceRequest);

            return new PaymentPreferenceResponse(preference.getId(), preference.getInitPoint());

        } catch (MPApiException e) {
            // 🧠 Esto te da el cuerpo JSON de la respuesta con el detalle real del error
            String responseBody = e.getApiResponse() != null ? e.getApiResponse().getContent() : "Sin cuerpo";
            log.error("MPApiException al crear preferencia. Respuesta de la API: {}", responseBody, e);
            throw new PaymentGatewayException("Error de proveedor de pagos: preferencia inválida.");
        } catch (MPException e) {
            log.error("MPException al comunicarse con MercadoPago", e);
            throw new PaymentGatewayException("Error al contactar al proveedor de pagos.");
        }
    }


    @Override
    public PaymentStatusResponse checkStatus(String paymentId) {
        return new PaymentStatusResponse(/*...*/);
    }
}
