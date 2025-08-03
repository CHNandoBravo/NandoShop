package shop.nandoShop.nandoshop_app.services.interfaces;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import shop.nandoShop.nandoshop_app.dtos.PreferenceDTO;
import shop.nandoShop.nandoshop_app.dtos.responses.PaymentPreferenceResponse;
import shop.nandoShop.nandoshop_app.dtos.responses.PaymentStatusResponse;

public interface PaymentGateway {
    PaymentPreferenceResponse createPreference(PreferenceDTO request) throws MPException, MPApiException;
    PaymentStatusResponse checkStatus(String paymentId);
}
