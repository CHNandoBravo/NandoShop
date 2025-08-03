package shop.nandoShop.nandoshop_app.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.nandoShop.nandoshop_app.dtos.requests.PaymentRequest;
import shop.nandoShop.nandoshop_app.dtos.responses.PaymentPreferenceResponse;
import shop.nandoShop.nandoshop_app.services.interfaces.PaymentService;

@RestController
@RequestMapping("/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<PaymentPreferenceResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentPreferenceResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
