package shop.nandoShop.nandoshop_app.services.interfaces;

import shop.nandoShop.nandoshop_app.dtos.requests.PaymentRequest;
import shop.nandoShop.nandoshop_app.dtos.responses.PaymentPreferenceResponse;

public interface PaymentService {
    PaymentPreferenceResponse createPayment(PaymentRequest request);
}
